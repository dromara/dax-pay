package cn.daxpay.open.payment.old.pay.service.assist;

import cn.daxpay.open.payment.common.service.MerchantContextLoader;
import cn.daxpay.open.payment.core.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.core.strategy.auth.AbsChannelAuthStrategy;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthStatusEnum;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/// # 通道认证服务, 用户获取OpenId或UserId等新鲜
///
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelAuthService {
    private final RedisTemplate<String, Object> redisTemplate;
    public static final String CHANNEL_AUTH_KEY_PREFIX = "payment:channel-auth:";
    private final MerchantContextLoader merchantContextLoader;

    /// 获取授权链接
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param) {
        merchantContextLoader.initMchByApp(param.getAppId());
        var strategy = PaymentStrategyFactory.create(param.getChannel(), AbsChannelAuthStrategy.class);
        AuthUrlResult authUrlResult = strategy.generateAuthUrl(param);
        // 如果返回有查询Code值, 将结果写入Redis中
        if (StrUtil.isNotBlank(authUrlResult.getQueryCode())) {
            AuthResult authResult = new AuthResult().setStatus(ChannelAuthStatusEnum.WAITING.getCode());
            redisTemplate.opsForValue().set(CHANNEL_AUTH_KEY_PREFIX + authUrlResult.getQueryCode(), authResult, 5, TimeUnit.MINUTES);
        }
        return authUrlResult;
    }

    /// 通过AuthCode获取认证结果
    public AuthResult auth(AuthCodeParam param) {
        merchantContextLoader.initMchByApp(param.getAppId());
        var strategy = PaymentStrategyFactory.create(param.getChannel(), AbsChannelAuthStrategy.class);
        AuthResult authResult = strategy.doAuth(param);
        authResult.setStatus(ChannelAuthStatusEnum.SUCCESS.getCode());
        if (StrUtil.isNotBlank(param.getQueryCode())) {
            redisTemplate.opsForValue().set(CHANNEL_AUTH_KEY_PREFIX + param.getQueryCode(), authResult, 5, TimeUnit.MINUTES);
        }
        return authResult;
    }

    /// 通过查询码获取认证结果
    public AuthResult queryAuthResult(String queryCode) {
        // 从redis中获取，读取后显式转换为目标类型
        var authResult = redisTemplate.opsForValue().get(CHANNEL_AUTH_KEY_PREFIX + queryCode);
        if (Objects.isNull(authResult)) {
            return new AuthResult().setStatus(ChannelAuthStatusEnum.NOT_EXIST.getCode());
        }
        return JacksonUtil.convert(authResult, AuthResult.class);
    }
}
