package cn.daxpay.open.payment.auth;

import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/// # 通道认证 OAuth 回调路径
///
/// 收敛三通道(支付宝 / 微信公众号 / 抖音 H5)的固定回调路径常量与 redirect_uri 拼装逻辑,
/// 消除原 [PlatformAuthService] 三个常量(ALIPAY_AUTH_PATH/WECHAT_AUTH_PATH/DOUYIN_AUTH_PATH)与
/// 各通道策略(WechatIsvAuthStrategy / WechatDirectAuthStrategy / DouyinDirectAuthStrategy)中
/// 重复的 gatewayBase 空检查 + `removeSuffix("/") + 字面量路径` 拼装。
///
/// ## 约定
/// - 回调路径固定(不含动态段),会话标识 authToken 通过 OAuth state 参数透传,回调后从 state 恢复
/// - 各第三方平台(支付宝/微信/抖音)开放平台后台需登记对应路径
///
/// ## 前后端契约(重要)
/// 这三个路径是后端拼装 redirect_uri 用的 path 段, 同时也是前端 H5 落地页路由的约定
/// (见 `dax-pay-h5/src/shared/router/paths.ts`)。改名需前后端同步。
@Getter
@AllArgsConstructor
public enum AuthRedirectUri {

    /// 支付宝 OAuth 认证回调路径
    ALIPAY("/auth/alipay"),

    /// 微信公众号 OAuth 认证回调路径(直连与服务商共用)
    WECHAT("/auth/wechat"),

    /// 抖音 H5 silent_auth 认证回调路径
    DOUYIN("/auth/douyin");

    private final String path;


    /// 拼接完整回调地址: `{paymentGatewayBaseUrl}{path}`
    ///
    /// 读取平台端点配置 [PlatformUrlConfigService] 的 paymentGatewayBaseUrl,
    /// 去尾斜杠后拼接本枚举的 path。gatewayBase 未配置时抛 gatewayUrlNotConfigured。
    ///
    /// @param urlConfigService 平台端点配置服务
    /// @return 完整 redirect_uri, 如 `https://gw.example.com/auth/wechat`
    public String buildRedirectUri(PlatformUrlConfigService urlConfigService) {
        String base = urlConfigService.getUrlConfig().getPaymentGatewayBaseUrl();
        if (StrUtil.isBlank(base)) {
            // 支付网关前端地址未配置
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.gatewayUrlNotConfigured");
        }
        return StrUtil.removeSuffix(base, "/") + path;
    }
}
