package cn.daxpay.open.platform.capability.social.auth;

import cn.daxpay.open.platform.capability.social.justauth.request.DingTalkRequest;
import cn.daxpay.open.platform.capability.social.justauth.request.DouyinRequest;
import cn.daxpay.open.platform.capability.social.justauth.request.FeishuRequest;
import cn.daxpay.open.platform.capability.social.justauth.request.GithubRequest;
import cn.daxpay.open.platform.capability.social.justauth.request.GiteeRequest;
import cn.daxpay.open.platform.capability.social.justauth.request.GoogleRequest;
import cn.daxpay.open.platform.capability.social.justauth.request.QqRequest;
import cn.daxpay.open.platform.capability.social.justauth.request.SocialAuthRequest;
import cn.daxpay.open.platform.capability.social.justauth.request.WeComRequest;
import cn.daxpay.open.platform.capability.social.justauth.request.WechatMpRequest;
import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import org.springframework.stereotype.Component;

/// # 社交授权请求工厂
///
/// 仅负责按平台枚举创建对应的 [SocialAuthRequest] 实现.
/// 配置加载由调用方(SocialEndpoint)通过 SocialConfigService 完成, 工厂不再耦合业务配置.
///
@Component
public class SocialAuthRequestFactory {

    /// 根据平台来源创建对应的请求实现
    public SocialAuthRequest create(SocialSourceEnum source, SocialAuthConfig config) {
        return switch (source) {
            case GITHUB -> new GithubRequest(config);
            case GITEE -> new GiteeRequest(config);
            case GOOGLE -> new GoogleRequest(config);
            case QQ -> new QqRequest(config);
            case WECHAT_MP -> new WechatMpRequest(config);
            case WE_COM -> new WeComRequest(config);
            case FEISHU -> new FeishuRequest(config);
            case DINGTALK -> new DingTalkRequest(config);
            case DOUYIN -> new DouyinRequest(config);
            // 支付宝非标准 OAuth2, 不走 JustAuth 工厂, 由 iam 模块 AlipayAuthEndpoint 独立处理
            case ALIPAY -> throw new OperationFailException("error.social.alipayUseDedicatedEndpoint");
        };
    }

    /// 根据平台来源创建对应的请求实现, 平台不支持时抛错
    public SocialAuthRequest create(String sourceName, SocialAuthConfig config) {
        SocialSourceEnum source = SocialSourceEnum.of(sourceName);
        if (source == null) {
            // 社交登录: 不支持的平台
            throw new OperationFailException("error.social.unsupportedSource");
        }
        return this.create(source, config);
    }
}
