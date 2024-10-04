package org.dromara.daxpay.service.service.assist;

import org.dromara.daxpay.core.enums.ChannelAuthStatusEnum;
import org.dromara.daxpay.core.param.assist.AuthCodeParam;
import org.dromara.daxpay.core.param.assist.GenerateAuthUrlParam;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.core.result.assist.AuthUrlResult;
import org.dromara.daxpay.service.strategy.AbsChannelAuthStrategy;
import org.dromara.daxpay.service.util.PaymentStrategyFactory;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 通道认证服务, 用户获取OpenId或UserId等新鲜
 * @author xxm
 * @since 2024/9/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelAuthService {
    private final RedisTemplate<String, AuthResult> redisTemplate;
    public static final String CHANNEL_AUTH_KEY_PREFIX = "daxpay:channel:auth:";

    /**
     * 获取授权链接
     */
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param) {
        var strategy = PaymentStrategyFactory.create(param.getChannel(), AbsChannelAuthStrategy.class);
        AuthUrlResult authUrlResult = strategy.generateAuthUrl(param);
        // 如果返回有查询Code值, 将结果写入Redis中
        if (StrUtil.isNotBlank(authUrlResult.getQueryCode())){
            AuthResult authResult = new AuthResult().setStatus(ChannelAuthStatusEnum.WAITING.getCode());
            redisTemplate.opsForValue().set(CHANNEL_AUTH_KEY_PREFIX + authUrlResult.getQueryCode(), authResult, 5, TimeUnit.MINUTES);
        }
        return authUrlResult;
    }

    /**
     * 通过AuthCode获取认证结果
     */
    public AuthResult auth(AuthCodeParam param) {
        var strategy = PaymentStrategyFactory.create(param.getChannel(), AbsChannelAuthStrategy.class);
        AuthResult authResult = strategy.doAuth(param);
        authResult.setStatus(ChannelAuthStatusEnum.SUCCESS.getCode());
        if (StrUtil.isNotBlank(param.getQueryCode())){
            redisTemplate.opsForValue().set(CHANNEL_AUTH_KEY_PREFIX + param.getQueryCode(), authResult, 5, TimeUnit.MINUTES);
        }
        return authResult;
    }

    /**
     * 通过查询码获取认证结果
     */
    public AuthResult queryAuthResult(String queryCode) {
        // 从redis中获取
        var authResult =redisTemplate.opsForValue().get(CHANNEL_AUTH_KEY_PREFIX + queryCode);
        if (Objects.isNull(authResult)){
            return new AuthResult().setStatus(ChannelAuthStatusEnum.NOT_EXIST.getCode());
        }
        return authResult;
    }
}
