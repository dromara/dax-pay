package cn.daxpay.open.payment.admin.check.service;

import cn.daxpay.open.payment.admin.controller.check.ProductBindingCheckController;
import cn.daxpay.open.payment.common.check.checker.ProductBindingChecker;
import cn.daxpay.open.payment.common.check.model.ProductBindingCheckItem;
import cn.daxpay.open.payment.common.check.model.ProductBindingCheckResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 产品级绑定检查聚合服务
///
/// 自动收集所有 [ProductBindingChecker] 实现, 按支付产品编码路由到对应检查器。
/// 单个检查器抛异常时降级返回空结果, 不影响其他产品。
///
/// 供运营端产品绑定检查 Controller 调用, 前端在进入服务商产品配置页时拉取检查结果。
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductBindingCheckService {

    private final List<ProductBindingChecker> checkers;

    /// 检查指定产品的配置绑定完整性
    public ProductBindingCheckResult check(String product) {
        ProductBindingChecker checker = checkers.stream()
                .filter(c -> c.getProduct().equals(product))
                .findFirst()
                .orElse(null);
        if (checker == null) {
            // 无对应产品的检查器, 返回空结果
            return ProductBindingCheckResult.empty(product);
        }
        try {
            List<ProductBindingCheckItem> items = checker.check();
            int configuredCount = (int) items.stream().filter(ProductBindingCheckItem::isConfigured).count();
            return new ProductBindingCheckResult()
                    .setProduct(product)
                    .setItems(items)
                    .setConfiguredCount(configuredCount)
                    .setTotalCount(items.size())
                    .setAllConfigured(configuredCount == items.size());
        } catch (Exception e) {
            log.warn("产品绑定检查器执行失败 product={} checker={} error={}",
                    product, checker.getClass().getSimpleName(), e.getMessage());
            return ProductBindingCheckResult.empty(product);
        }
    }
}
