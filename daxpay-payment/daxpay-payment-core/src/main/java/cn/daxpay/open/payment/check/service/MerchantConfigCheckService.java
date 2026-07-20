package cn.daxpay.open.payment.check.service;

import cn.daxpay.open.payment.check.checker.MerchantConfigChecker;
import cn.daxpay.open.payment.check.enums.ConfigCheckCategoryEnum;
import cn.daxpay.open.payment.check.model.ConfigCheckItem;
import cn.daxpay.open.payment.check.model.ConfigCheckResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/// # 商户端配置检查聚合服务
///
/// 自动收集所有 [MerchantConfigChecker] 实现, 按商户号执行检测并聚合结果。
/// 检测为纯计算无状态, 配置变更后立即生效(无缓存延迟)。
/// 单个检查器抛异常时降级跳过, 不影响其他维度检测。
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantConfigCheckService {

    private final List<MerchantConfigChecker> checkers;

    /// 检测指定商户的未完成配置项
    public ConfigCheckResult check(String mchNo) {
        if (mchNo == null || mchNo.isBlank()) {
            return ConfigCheckResult.empty();
        }
        List<ConfigCheckItem> items = new ArrayList<>();
        Map<String, Integer> categoryCounts = new LinkedHashMap<>();
        for (MerchantConfigChecker checker : checkers) {
            ConfigCheckItem item = safeCheck(checker, mchNo);
            if (item != null) {
                items.add(item);
                categoryCounts.merge(item.getCategory(), 1, Integer::sum);
            }
        }
        return new ConfigCheckResult()
                .setItems(items)
                .setTotalCount(items.size())
                .setCategoryCounts(categoryCounts);
    }

    /// 单个检查器执行保护: 抛异常时记录日志并返回 null(视为该维度已配置或不可检测)
    private ConfigCheckItem safeCheck(MerchantConfigChecker checker, String mchNo) {
        try {
            return checker.check(mchNo);
        } catch (Exception e) {
            log.warn("商户配置检查器执行失败 mchNo={} checker={} error={}",
                    mchNo, checker.getClass().getSimpleName(), e.getMessage());
            return null;
        }
    }

    /// 仅返回未配置总数(供 Header 角标等轻量场景, 避免重复聚合)
    public int count(String mchNo) {
        return check(mchNo).getTotalCount();
    }

    /// 返回支持的分类列表(供前端分类标签渲染)
    public List<ConfigCheckCategoryEnum> supportedCategories() {
        return checkers.stream().map(MerchantConfigChecker::getCategory).distinct().toList();
    }
}
