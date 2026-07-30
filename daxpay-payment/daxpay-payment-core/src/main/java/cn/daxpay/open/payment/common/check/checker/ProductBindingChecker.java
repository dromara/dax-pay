package cn.daxpay.open.payment.common.check.checker;

import cn.daxpay.open.payment.common.check.model.ProductBindingCheckItem;

import java.util.List;

/// # 产品级绑定检查器接口
///
/// 每个实现类负责一个"服务商支付产品"的配置完整性检测。
/// 实现类需标注 `@Component`, 由 [cn.daxpay.open.payment.admin.check.service.ProductBindingCheckService]
/// 自动收集, 按 [#getProduct] 路由到对应产品。
///
/// 与 [AdminConfigChecker] / [MerchantConfigChecker] 的区别:
/// - 前两者是"是否有未完成配置"(返回未完成项, 已完成返回 null)
/// - 本接口是"逐项报告配置状态"(返回全部检查项及其 configured 标记),
///   供前端在产品配置页展示绑定进度。
public interface ProductBindingChecker {

    /// 该检查器负责的支付产品编码(如 `wechat_isv`、`alipay_isv`)
    String getProduct();

    /// 执行绑定检查, 返回各项配置状态(包含已配置与未配置)
    List<ProductBindingCheckItem> check();
}
