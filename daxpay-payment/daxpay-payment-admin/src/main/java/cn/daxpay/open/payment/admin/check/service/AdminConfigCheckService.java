package cn.daxpay.open.payment.admin.check.service;

import cn.daxpay.open.payment.common.check.checker.AdminConfigChecker;
import cn.daxpay.open.payment.common.check.model.ConfigCheckItem;
import cn.daxpay.open.payment.common.check.model.ConfigCheckResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/// # 运营端配置检查聚合服务
///
/// 自动收集所有 [AdminConfigChecker] 实现, 执行检测并聚合结果。
/// 检测为纯计算无状态, 配置变更后立即生效(无缓存延迟)。
/// 单个检查器抛异常时降级跳过, 不影响其他维度检测。
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminConfigCheckService {

    private final List<AdminConfigChecker> checkers;

    /// 检测平台级未完成配置项
    public ConfigCheckResult check() {
        List<ConfigCheckItem> items = new ArrayList<>();
        Map<String, Integer> categoryCounts = new LinkedHashMap<>();
        for (AdminConfigChecker checker : checkers) {
            ConfigCheckItem item = safeCheck(checker);
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
    private ConfigCheckItem safeCheck(AdminConfigChecker checker) {
        try {
            return checker.check();
        } catch (Exception e) {
            log.warn("运营配置检查器执行失败 checker={} error={}",
                    checker.getClass().getSimpleName(), e.getMessage());
            return null;
        }
    }
}
