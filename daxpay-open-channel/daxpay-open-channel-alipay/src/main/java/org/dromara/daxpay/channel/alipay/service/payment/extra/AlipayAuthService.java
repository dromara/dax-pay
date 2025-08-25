package org.dromara.daxpay.channel.alipay.service.payment.extra;

import cn.bootx.platform.core.exception.BizException;
import org.dromara.daxpay.channel.alipay.entity.config.AliPayConfig;
import org.dromara.daxpay.channel.alipay.service.payment.config.AlipayConfigService;
import org.dromara.daxpay.core.context.PaymentReqInfoLocal;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.core.result.assist.AuthUrlResult;
import org.dromara.daxpay.service.pay.common.local.PaymentContextLocal;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付宝认证服务类
 * @author xxm
 * @since 2024/6/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayAuthService {

    private final AlipayConfigService aliPayConfigService;

    /**
     * 生成一个用于授权页面的链接
     * 1. 如果手动传输授权回调路径, 拼接传输的授权回调路径
     * 2. 如果未手动传输授权回调路径, 使用系统默认的授权页路径进行拼接
     */
    public AuthUrlResult generateAuthUrl(String authPath, String channel, String appId, String aliAppId) {
        String queryCode = RandomUtil.randomString(10);
        PaymentReqInfoLocal reqInfo = PaymentContextLocal.get().getReqInfo();
        String serverUrl = reqInfo.getGatewayH5Url();
        if (StrUtil.isBlank(authPath)){
            authPath = StrUtil.format("/auth/alipay/{}/{}/{}/{}", appId, channel, queryCode, aliAppId);
        }
        String authUrl = StrUtil.format("{}{}", serverUrl, authPath);
        return new AuthUrlResult().setAuthUrl(authUrl).setQueryCode(queryCode);
    }

    /**
     * 获取OpenId或者userid用户标识
     */
    @SneakyThrows
    public AuthResult getOpenIdOrUserId(String authCode, AliPayConfig aliPayConfig) {
        // 初始化SDK
        // 构造请求参数以调用接口
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        // 设置授权码
        request.setCode(authCode);
        // 设置授权方式
        request.setGrantType("authorization_code");
        var response = aliPayConfigService.execute(request,aliPayConfig);
        if (!response.isSuccess()) {
            log.warn("获取支付宝OpenId失败,原因:{}", response.getSubMsg());
            throw new BizException("获取支付宝OpenId失败");
        }
        var result = new AuthResult();
        // 如果未申请 OpenId 方式, 则获取的是UserId
        if (StrUtil.isBlank(response.getOpenId())) {
            return result.setUserId(response.getUserId());
        }
        return result.setOpenId(response.getOpenId());
    }
}
