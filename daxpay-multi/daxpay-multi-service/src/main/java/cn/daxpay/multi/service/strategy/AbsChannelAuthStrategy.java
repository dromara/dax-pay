package cn.daxpay.multi.service.strategy;

import cn.daxpay.multi.core.param.assist.AuthCodeParam;
import cn.daxpay.multi.core.param.assist.GenerateAuthUrlParam;
import cn.daxpay.multi.core.result.assist.AuthResult;
import cn.daxpay.multi.core.result.assist.AuthUrlResult;

/**
 * 通道抽象认证策略,
 * @author xxm
 * @since 2024/9/24
 */
public abstract class AbsChannelAuthStrategy implements PaymentStrategy {

    /**
     * 获取授权链接
     */
    public abstract AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param);

    /**
     * 通过AuthCode获取认证结果
     */
    public abstract AuthResult doAuth(AuthCodeParam param);


}
