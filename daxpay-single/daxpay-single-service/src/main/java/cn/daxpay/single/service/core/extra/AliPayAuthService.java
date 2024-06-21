package cn.daxpay.single.service.core.extra;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.redis.RedisClient;
import cn.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import cn.daxpay.single.service.core.channel.alipay.service.AliPayConfigService;
import cn.daxpay.single.service.core.system.config.entity.PlatformConfig;
import cn.daxpay.single.service.core.system.config.service.PlatformConfigService;
import cn.daxpay.single.service.dto.extra.AuthUrlResult;
import cn.daxpay.single.service.dto.extra.OpenIdResult;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 支付宝认证服务类
 * @author xxm
 * @since 2024/6/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayAuthService {

    public static final String OPEN_ID_KEY_PREFIX = "daxpay:wechat:openId:";

    private final AliPayConfigService aliPayConfigService;

    private final PlatformConfigService platformsConfigService;

    private final RedisClient redisClient;

    /**
     * 根据传入的标识码code生成一个用于微信授权页面的链接
     */
    public AuthUrlResult generateAuthUrl() {
        PlatformConfig platformConfig = platformsConfigService.getConfig();
        AliPayConfig aliPayConfig = aliPayConfigService.getConfig();
        String code = RandomUtil.randomString(10);
        // 默认读取通道配置
        String serverUrl = aliPayConfig.getRedirectUrl();
        // 如果未配置, 读取平台配置
        if (StrUtil.isBlank(serverUrl)) {
            serverUrl = platformConfig.getWebsiteUrl();
        }
        // 构建出授权成功后重定向页面链接
        String redirectUrl = StrUtil.format("{}/unipay/callback/alipay/auth/{}", platformConfig.getWebsiteUrl(), code);
        // 构造出授权页地址
        String authUrl = StrUtil.format("{}/h5/alipayAuth.html?appId={}&redirectUrl={}",
                platformConfig.getWebsiteUrl(), aliPayConfig.getAppId(), redirectUrl);
        // 写入Redis, 五分钟有效期
        redisClient.setWithTimeout(OPEN_ID_KEY_PREFIX + code, "", 5*60*1000L);
        return new AuthUrlResult()
                .setCode(code)
                .setAuthUrl(authUrl);
    }

    /**
     * 微信授权回调页面, 通过获取到authCode获取到OpenId, 存到到Redis中对应code关联的键值对中
     * @param authCode 微信返回的授权码
     * @param code 标识码
     */
    public void authCallback(String authCode, String code) {
        // 获取OpenId
        String openId = this.getOpenId(authCode);
        // 写入Redis
        redisClient.setWithTimeout(OPEN_ID_KEY_PREFIX + code, openId, 60*1000L);
    }

    /**
     * 通过标识码轮训获取OpenId
     */
    public OpenIdResult queryOpenId(String code) {
        // 从redis中获取
        String openId = redisClient.get(OPEN_ID_KEY_PREFIX + code);
        // 不为空存在
        if (StrUtil.isNotBlank(openId)){
            return new OpenIdResult().setOpenId(openId).setStatus("success");
        }
        // 为空获取中
        if (Objects.equals(openId, "")){
            return new OpenIdResult().setStatus("pending");
        }
        // null不存在
        return new OpenIdResult().setStatus("fail");
    }


    @SneakyThrows
    public String getOpenId(String authCode) {
        // 初始化SDK
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient();
        // 构造请求参数以调用接口
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        // 设置授权码
        request.setCode(authCode);
        // 设置授权方式
        request.setGrantType("authorization_code");
        AlipaySystemOauthTokenResponse response = alipayClient.execute(request);
        if (!response.isSuccess()) {
            log.warn("获取支付宝OpenId失败,原因:{}", response.getSubMsg());
            throw new BizException("获取支付宝OpenId失败");
        }
        return response.getOpenId();
    }
}
