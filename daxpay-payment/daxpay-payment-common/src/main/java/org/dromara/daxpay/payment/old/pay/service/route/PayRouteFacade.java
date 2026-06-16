package org.dromara.daxpay.payment.old.pay.service.route;

import org.dromara.daxpay.payment.unipay.param.trade.pay.PayParam;

/// # 支付通道路由门面
///
public interface PayRouteFacade {

    /// 当 product 为空时执行路由并回填支付参数
    void resolve(PayParam payParam);
}
