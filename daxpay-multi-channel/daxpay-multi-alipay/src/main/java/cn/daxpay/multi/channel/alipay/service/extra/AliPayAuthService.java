package cn.daxpay.multi.channel.alipay.service.extra;

import cn.bootx.platform.core.exception.BizException;
import cn.daxpay.multi.channel.alipay.entity.config.AliPayConfig;
import cn.daxpay.multi.channel.alipay.service.config.AliPayConfigService;
import cn.daxpay.multi.core.param.assist.GenerateAuthUrlParam;
import cn.daxpay.multi.core.result.assist.AuthUrlResult;
import cn.daxpay.multi.core.result.assist.OpenIdResult;
import cn.daxpay.multi.service.entity.config.PlatformConfig;
import cn.daxpay.multi.service.service.config.PlatformConfigService;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    public static final String OPEN_ID_KEY_PREFIX = "daxpay:alipay:openId:";

    private final AliPayConfigService aliPayConfigService;

    private final PlatformConfigService platformsConfigService;

    private final StringRedisTemplate redisTemplate;

    /**
     * 生成一个用于授权页面的链接
     * 1. 如果手动传输授权回调地址, 不进行处理
     * 2. 如果未手动传输授权回调地址, 使用系统默认的授权页地址, 返回授权页地址和用于查询的标识码
     */
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param) {

        // 如果手动传输, 拼接授权地址后直接返回
        if (StrUtil.isAllNotBlank(param.getAuthRedirectUrl())){
            return new AuthUrlResult();
        } else {
            PlatformConfig platformConfig = platformsConfigService.getConfig();
            AliPayConfig aliPayConfig = aliPayConfigService.getAliPayConfig();
            String code = RandomUtil.randomString(10);
            // 读取平台网关配置
            String serverUrl = platformConfig.getGatewayServiceUrl();
            // 构建出授权成功后重定向页面链接
            String redirectUrl = StrUtil.format("{}/unipay/callback/alipay/auth/{}", serverUrl, code);
            // 构造出授权页地址
            String authUrl = StrUtil.format("{}/h5/alipayAuth.html?appId={}&redirectUrl={}",
                    serverUrl, aliPayConfig.getAppId(), redirectUrl);
            // 写入Redis, 五分钟有效期
            redisTemplate.opsForValue().set(OPEN_ID_KEY_PREFIX + code, "", 5*60*1000L);
            return new AuthUrlResult()
                    .setCode(code)
                    .setAuthUrl(authUrl);
        }
    }

    /**
     * 支付宝权回调页面, 通过获取到authCode获取到OpenId, 存到到Redis中对应code关联的键值对中
     * @param authCode 微信返回的授权码
     * @param code 标识码
     */
    public void authCallback(String authCode, String code) {
        // 获取OpenId或UserId
        String openId = this.getOpenIdOrUserId(authCode);
        // 写入Redis
        redisTemplate.opsForValue().set(OPEN_ID_KEY_PREFIX + code, openId, 60*1000L);
    }

    /**
     * 通过标识码轮训获取OpenId或UserId
     */
    public OpenIdResult queryOpenId(String code) {
        // 从redis中获取
        String openId =redisTemplate.opsForValue().get(OPEN_ID_KEY_PREFIX + code);
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


    /**
     * 获取OpenId或者userid用户标识
     */
    @SneakyThrows
    public String getOpenIdOrUserId(String authCode) {
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
        // 如果未申请 OpenId 方式, 则获取的是UserId
        if (StrUtil.isBlank(response.getOpenId())) {
            return response.getUserId();
        }
        return response.getOpenId();
    }
}
