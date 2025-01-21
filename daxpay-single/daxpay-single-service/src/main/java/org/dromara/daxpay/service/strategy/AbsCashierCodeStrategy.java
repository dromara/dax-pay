package org.dromara.daxpay.service.strategy;

import org.dromara.daxpay.core.param.assist.AuthCodeParam;
import org.dromara.daxpay.core.param.cashier.CashierCodePayParam;
import org.dromara.daxpay.core.param.trade.pay.PayParam;
import org.dromara.daxpay.core.result.assist.AuthResult;

/**
 * 抽象收银码牌策略
 * @author xxm
 * @since 2024/9/28
 */
public abstract class AbsCashierCodeStrategy implements PaymentStrategy{

    /**
     * 生成授权链接, 主要是微信类通道使用, 用于获取OpenId
     */
    public String generateAuthUrl(String cashierCode) {
        return "";
    }

    /**Z
     * 获取认证结果
     */
    public AuthResult doAuth(AuthCodeParam param) {
        return new AuthResult();
    }

    /**
     * 支付参数处理
     */
    public void handlePayParam(CashierCodePayParam cashierPayParam, PayParam payParam) {
    }

}
