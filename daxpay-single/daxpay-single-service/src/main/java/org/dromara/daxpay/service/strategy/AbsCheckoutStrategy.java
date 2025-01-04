package org.dromara.daxpay.service.strategy;

import org.dromara.daxpay.core.param.assist.AuthCodeParam;
import org.dromara.daxpay.core.param.checkout.CheckoutPayParam;
import org.dromara.daxpay.core.param.trade.pay.PayParam;
import org.dromara.daxpay.core.result.assist.AuthResult;

/**
 * 抽象收银台策略
 * @author xxm
 * @since 2024/12/2
 */
public abstract class AbsCheckoutStrategy implements PaymentStrategy{

    /**
     * 生成授权链接, 主要是微信类通道使用, 用于获取OpenId
     */
    public String generateAuthUrl(String orderNo) {
        return "";
    }

    /**
     * 检测付款码
     */
    public boolean checkBarCode(String barCode){
        return false;
    }

    /**
     * 获取认证结果
     */
    public AuthResult doAuth(AuthCodeParam param) {
        return new AuthResult();
    }

    /**
     * 支付参数处理
     */
    public void handlePayParam(CheckoutPayParam cashierPayParam, PayParam payParam) {
    }
}
