package cn.daxpay.open.payment.strategy.auth;

import cn.daxpay.open.payment.common.strategy.PaymentStrategy;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;

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
