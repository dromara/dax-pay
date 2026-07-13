package cn.daxpay.open.payment.route;

import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;

/// # 支付路由门面接口
///
/// 定义支付路由的抽象入口，供核心支付流程（common 模块）依赖。
/// 实现在 merchant 模块（PayRouteService），按应用路由策略匹配并回填产品。
public interface PayRouteFacade {

    /// 解析支付路由：直定模式(已传 channelMchNo)直接解析，否则按策略模式匹配并回填。
    /// 调用方需保证 appId 已解析完毕。
    void resolve(NormalPayParam payParam);
}
