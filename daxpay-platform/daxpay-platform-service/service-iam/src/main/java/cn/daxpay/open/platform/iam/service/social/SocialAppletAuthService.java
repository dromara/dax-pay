package cn.daxpay.open.platform.iam.service.social;

import java.util.Collections;
import java.util.Map;

import cn.daxpay.open.platform.capability.alipay.auth.config.AlipayAuthConfig;
import cn.daxpay.open.platform.capability.alipay.auth.result.AlipayAuthResult;
import cn.daxpay.open.platform.capability.alipay.auth.service.AlipayAuthCapability;
import cn.daxpay.open.platform.capability.douyin.auth.result.DouyinAuthResult;
import cn.daxpay.open.platform.capability.douyin.auth.service.DouyinMaAuthService;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatAuthResult;
import cn.daxpay.open.platform.capability.wechat.auth.service.WechatMaAuthService;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.iam.entity.social.SocialLoginConfig;
import cn.daxpay.open.platform.iam.result.social.AppletAuthResult;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 小程序快捷登录认证服务
///
/// 小程序端(微信/支付宝/抖音)通过 uni.login 等获取平台登录 code 后直传,
/// 本服务按 source 分发到 capability 能力层换取 openId。
///
/// 与 redirect 模式([SocialLoginService]) 的差异: 无授权地址与回调, code 由前端直传;
/// 与收银台链路([cn.daxpay.open.payment.trade.runtime.service.pay.gateway.CashierAuthService]) 的差异:
/// 凭据来源为 iam_social_login_config(source 维度), 而非收银台的 pay_platform_mobile_app(appType 维度)。
///
/// 凭据落位约定(iam_social_login_config):
/// - 微信/抖音小程序: clientId=appId, clientSecret=appSecret(加密存储)
/// - 支付宝小程序: clientId=appId, clientSecret=应用私钥(加密存储),
///   其余鉴权参数(authType/支付宝公钥/证书内容)存 extra jsonb
///
@Slf4j
@Service
@RequiredArgsConstructor
public class SocialAppletAuthService {

    private final SocialLoginConfigService socialLoginConfigService;

    private final WechatMaAuthService wechatMaAuthService;

    private final AlipayAuthCapability alipayAuthCapability;

    private final DouyinMaAuthService douyinMaAuthService;

    /// 用平台登录 code 换取用户标识(openId)
    ///
    /// 仅接受 applet 型 source, 配置须已配置且已启用(configured + enabled)。
    public AppletAuthResult exchangeOpenId(String source, String code) {
        SocialSourceEnum socialSource = SocialSourceEnum.of(source);
        if (socialSource == null || !socialSource.isApplet()) {
            // 社交登录: 不支持的平台
            throw new OperationFailException("error.social.unsupportedSource");
        }
        if (StrUtil.isBlank(code)) {
            // 社交登录: 授权码不能为空
            throw new OperationFailException("error.social.authCodeBlank");
        }
        SocialLoginConfig config = socialLoginConfigService.findEnabledBySource(source);
        if (config == null) {
            // 社交登录: 平台未配置或未启用
            throw new OperationFailException("error.social.configNotExist");
        }
        try {
            return switch (socialSource) {
                case WECHAT_APPLET -> this.exchangeWechat(config, code);
                case ALIPAY_APPLET -> this.exchangeAlipay(config, code);
                case DOUYIN_APPLET -> this.exchangeDouyin(config, code);
                // 前置 isApplet 校验保证不会走到此分支, 保险兜底
                default -> throw new OperationFailException("error.social.unsupportedSource");
            };
        }
        catch (OperationFailException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("小程序快捷登录换取用户标识失败: source={}, msg={}", source, e.getMessage(), e);
            // 社交登录: 登录凭证(code)换取平台用户标识失败
            throw new OperationFailException("error.social.codeExchangeFail", e.getMessage());
        }
    }

    /// 微信小程序: jscode2session 换 openId
    private AppletAuthResult exchangeWechat(SocialLoginConfig config, String code) {
        WechatAuthResult data = wechatMaAuthService.getOpenId(code, config.getClientId(), config.getClientSecret());
        if (StrUtil.isBlank(data.getOpenId())) {
            throw new OperationFailException("error.social.codeExchangeFail", "openId is blank");
        }
        return new AppletAuthResult()
                .setOpenId(data.getOpenId());
    }

    /// 支付宝小程序: alipay.system.oauth.token 换 userId(优先)/openId
    private AppletAuthResult exchangeAlipay(SocialLoginConfig config, String code) {
        AlipayAuthConfig authConfig = this.toAlipayAuthConfig(config);
        if (!alipayAuthCapability.isConfigured(authConfig)) {
            // 社交登录: 小程序登录平台凭据不完整
            throw new OperationFailException("error.social.appletNotConfigured");
        }
        AlipayAuthResult data = alipayAuthCapability.getUserId(authConfig, code);
        // 优先 userId(传统), 其次 openId(新标准)
        String userId = StrUtil.blankToDefault(data.getUserId(), data.getOpenId());
        if (StrUtil.isBlank(userId)) {
            throw new OperationFailException("error.social.codeExchangeFail", "userId is blank");
        }
        return new AppletAuthResult()
                .setOpenId(userId);
    }

    /// 抖音小程序: jscode2session(v2) 换 openId
    private AppletAuthResult exchangeDouyin(SocialLoginConfig config, String code) {
        DouyinAuthResult data = douyinMaAuthService.getOpenId(
                config.getClientId(), config.getClientSecret(), code, null);
        if (StrUtil.isBlank(data.getOpenId())) {
            throw new OperationFailException("error.social.codeExchangeFail", "openId is blank");
        }
        return new AppletAuthResult()
                .setOpenId(data.getOpenId());
    }

    /// iam 配置 → capability 层支付宝认证配置
    ///
    /// appId 取 clientId, 应用私钥取 clientSecret(加密存储),
    /// authType/支付宝公钥/证书内容存 extra jsonb(与 [SocialLoginConfigService#buildAuthConfig] 读取企业微信 agentId 同一机制)。
    private AlipayAuthConfig toAlipayAuthConfig(SocialLoginConfig config) {
        Map<String, String> extra = this.parseExtra(config.getExtra());
        return new AlipayAuthConfig()
                .setAppId(config.getClientId())
                .setAuthType(extra.get("authType"))
                .setPrivateKey(config.getClientSecret())
                .setAlipayPublicKey(extra.get("alipayPublicKey"))
                .setAppCert(extra.get("appCert"))
                .setAlipayCert(extra.get("alipayCert"))
                .setAlipayRootCert(extra.get("alipayRootCert"));
    }

    /// extra jsonb 原始文本 → Map(空容错)
    private Map<String, String> parseExtra(String extra) {
        if (StrUtil.isBlank(extra)) {
            return Collections.emptyMap();
        }
        return JSONUtil.toBean(extra, Map.class);
    }
}
