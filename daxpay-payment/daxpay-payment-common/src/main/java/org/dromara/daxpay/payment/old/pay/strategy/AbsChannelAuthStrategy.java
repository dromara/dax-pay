package org.dromara.daxpay.payment.old.pay.strategy;

import org.dromara.daxpay.payment.common.strategy.PaymentStrategy;
import org.dromara.daxpay.payment.unipay.param.assist.AuthCodeParam;
import org.dromara.daxpay.payment.unipay.param.assist.GenerateAuthUrlParam;
import org.dromara.daxpay.payment.unipay.result.assist.AuthResult;
import org.dromara.daxpay.payment.unipay.result.assist.AuthUrlResult;

/// # 通道抽象认证策略
///
public abstract class AbsChannelAuthStrategy implements PaymentStrategy {

    /// 获取授权链接
    public abstract AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param);

    /// 通过AuthCode获取认证结果
    public abstract AuthResult doAuth(AuthCodeParam param);

    /// 获取微信OpenId认证是否使用道通认证方式
    public boolean isWxChannelAuth() {
        return false;
    }

}
