package cn.daxpay.open.platform.capability.social.auth;

import cn.daxpay.open.platform.capability.social.justauth.request.DingTalkRequest;
import cn.daxpay.open.platform.capability.social.justauth.request.DouyinRequest;
import cn.daxpay.open.platform.capability.social.justauth.request.FeishuRequest;
import cn.daxpay.open.platform.capability.social.justauth.request.GithubRequest;
import cn.daxpay.open.platform.capability.social.justauth.request.GiteeRequest;
import cn.daxpay.open.platform.capability.social.justauth.request.QqRequest;
import cn.daxpay.open.platform.capability.social.justauth.request.SocialAuthRequest;
import cn.daxpay.open.platform.capability.social.justauth.request.WeComRequest;
import cn.daxpay.open.platform.capability.social.justauth.request.WechatMpRequest;
import cn.daxpay.open.platform.capability.social.config.entity.SocialConfig;
import cn.daxpay.open.platform.capability.social.config.service.SocialConfigService;
import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/// # 社交授权请求工厂
///
/// 根据平台来源构建对应的 SocialAuthRequest, 配置从 iam_social_config 表按终端加载
///
@Component
@RequiredArgsConstructor
public class SocialAuthRequestFactory {

    private final SocialConfigService socialConfigService;

    /// 根据平台构建授权请求(自动加载已配置且启用的配置)
    public SocialAuthRequest build(String sourceName) {
        SocialSourceEnum source = SocialSourceEnum.of(sourceName);
        if (source == null) {
            // 社交登录: 不支持的平台
            throw new OperationFailException("error.social.unsupportedSource");
        }
        SocialConfig config = socialConfigService.findEnabledBySource(sourceName);
        if (config == null) {
            // 社交登录: 平台未配置或未启用
            throw new OperationFailException("error.social.configNotExist");
        }
        SocialAuthConfig authConfig = socialConfigService.buildAuthConfig(config);
        return this.create(source, authConfig);
    }

    /// 根据平台来源创建对应的请求实现
    public SocialAuthRequest create(SocialSourceEnum source, SocialAuthConfig config) {
        return switch (source) {
            case GITHUB -> new GithubRequest(config);
            case GITEE -> new GiteeRequest(config);
            case QQ -> new QqRequest(config);
            case WECHAT_MP -> new WechatMpRequest(config);
            case WE_COM -> new WeComRequest(config);
            case FEISHU -> new FeishuRequest(config);
            case DINGTALK -> new DingTalkRequest(config);
            case DOUYIN -> new DouyinRequest(config);
        };
    }
}
