package cn.daxpay.open.payment.common.check.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 产品绑定检查汇总结果
///
/// 聚合单个产品的全部检查项, 供前端展示配置进度条与各项状态。
@Data
@Accessors(chain = true)
public class ProductBindingCheckResult {

    /// 支付产品编码
    private String product;

    /// 检查明细列表(包含已配置与未配置)
    private List<ProductBindingCheckItem> items;

    /// 已配置项数
    private int configuredCount;

    /// 总检查项数
    private int totalCount;

    /// 是否全部已配置
    private boolean allConfigured;

    public static ProductBindingCheckResult empty(String product) {
        return new ProductBindingCheckResult()
                .setProduct(product)
                .setItems(List.of())
                .setConfiguredCount(0)
                .setTotalCount(0)
                .setAllConfigured(false);
    }
}
