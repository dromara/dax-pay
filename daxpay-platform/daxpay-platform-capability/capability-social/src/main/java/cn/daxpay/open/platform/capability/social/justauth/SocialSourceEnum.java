package cn.daxpay.open.platform.capability.social.justauth;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/// # 第三方授权平台来源
///
/// 参考 JustAuth 的 AuthDefaultSource 重新实现, 仅保留系统所需的平台, 统一管理各平台的 OAuth2 端点地址.
/// 平台显示名通过 [I18nSupport] 国际化, 翻译文件位于 `enum/social_source.json`.
///
@Getter
@AllArgsConstructor
public enum SocialSourceEnum implements I18nSupport {


    /// 微信开放平台
    WECHAT_MP(
            "weChat",
            "https://open.weixin.qq.com/connect/oauth2/authorize",
            "https://api.weixin.qq.com/sns/oauth2/access_token",
            "https://api.weixin.qq.com/sns/userinfo"
    ),

    /// 企业微信(企业自建应用扫码)
    WE_COM(
            "weCom",
            "https://open.work.weixin.qq.com/wwopen/sso/qrConnect",
            "https://qyapi.weixin.qq.com/cgi-bin/gettoken",
            "https://qyapi.weixin.qq.com/cgi-bin/auth/getuserinfo"
    ),

    /// 飞书
    FEISHU(
            "feishu",
            "https://open.feishu.cn/open-apis/authen/v1/index",
            "https://open.feishu.cn/open-apis/authen/v1/access_token",
            "https://open.feishu.cn/open-apis/authen/v1/user_info"
    ),

    /// 钉钉(新版扫码登录)
    DINGTALK(
            "dingTalk",
            "https://login.dingtalk.com/oauth2/challenge.htm",
            "https://api.dingtalk.com/v1.0/oauth2/userAccessToken",
            "https://api.dingtalk.com/v1.0/contact/users/me"
    ),

    /// 支付宝(非标准 OAuth2, 不走 JustAuth 流程)
    /// 端点地址留空, 实际授权由 iam 模块的 AlipayAuthEndpoint 独立处理(调用 alipay.system.oauth.token)。
    /// 在 [SocialAuthRequestFactory] 中会对该 source 抛出"请使用支付宝专用端点"异常,
    /// 避免误走标准 OAuth2 工厂分支。
    /// 排序位于钉钉之后、抖音之前。
    ALIPAY("alipay", "", "", ""),

    /// 抖音(网站应用扫码登录)
    DOUYIN(
            "douyin",
            "https://open.douyin.com/platform/oauth/connect/",
            "https://open.douyin.com/oauth/access_token/",
            "https://open.douyin.com/oauth/userinfo/"
    ),

    /// QQ
    QQ(
            "qq",
            "https://graph.qq.com/oauth2.0/authorize",
            "https://graph.qq.com/oauth2.0/token",
            "https://graph.qq.com/user/get_user_info"
    ),

    /// GitHub
    GITHUB(
        "github",
        "https://github.com/login/oauth/authorize",
        "https://github.com/login/oauth/access_token",
        "https://api.github.com/user"
    ),

    /// Gitee 码云
    GITEE(
        "gitee",
        "https://gitee.com/oauth/authorize",
        "https://gitee.com/oauth/token",
        "https://gitee.com/api/v5/user"
    ),

    /// Google
    GOOGLE(
        "google",
        "https://accounts.google.com/o/oauth2/v2/auth",
        "https://oauth2.googleapis.com/token",
        "https://openidconnect.googleapis.com/v1/userinfo"
    ),

    ;

    /// 平台编码(与 AuthLoginTypeCode 对应, 作为配置和绑定的唯一标识)
    private final String code;

    /// 授权地址
    private final String authorizeUrl;

    /// 获取 accessToken 地址
    private final String accessTokenUrl;

    /// 获取用户信息地址
    private final String userInfoUrl;

    /// 枚举编码(用于国际化), 与 name 一致
    @Override
    public String getCode() {
        return code;
    }

    /// 翻译 key 前缀, 对应 enum/social_source.json
    @Override
    public String getI18nPrefix() {
        return "enum.social_source";
    }

    /// 授权地址
    public String authorize() {
        return authorizeUrl;
    }

    /// 获取 accessToken 地址
    public String accessToken() {
        return accessTokenUrl;
    }

    /// 获取用户信息地址
    public String userInfo() {
        return userInfoUrl;
    }

    /// 根据名称查找平台
    public static SocialSourceEnum of(String name) {
        return Arrays.stream(values())
                .filter(source -> source.code.equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    /// 是否"平台级跳转型"配置
    ///
    /// 此类平台(支付宝)非标准 OAuth2 或配置较重,
    /// 不在 iam_social_login_config 表内存 clientId/clientSecret,
    /// 而是使用独立的平台级配置(EncryptPlatformConfigTypeEnum / 独立表)。
    /// 前端登录配置抽屉据此隐藏 clientId/clientSecret, 仅保留启用开关与「前往配置凭据」入口。
    ///
    /// 新增此类平台时在此方法追加判断。
    public boolean isPlatformRedirect() {
        return this == ALIPAY;
    }
}
