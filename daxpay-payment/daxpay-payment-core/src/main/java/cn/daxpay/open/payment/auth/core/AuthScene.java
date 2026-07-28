package cn.daxpay.open.payment.auth.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import cn.daxpay.open.payment.auth.ChannelAuthService;

/// # 认证场景
///
/// 按业务目的将通道认证入口分为三类, 与签名方式、返回方式无关:
///
/// - **PAYMENT**: 支付认证 — 为完成 JSAPI/小程序支付获取用户标识(openId/userId)。
///   包括签名面 API(ChannelAuthController) 和网关 H5(GatewayClientController) 两个入口,
///   区别仅在安全验证方式, Service 层不区分。
///
/// - **PLATFORM**: 平台自用认证 — 系统自身功能需要的认证(调试验证配置 / 社交登录 / 消息通知等)。
///   使用平台级配置, 不依赖商户通道。其中调试走 ChannelAuthService; 社交登录和消息通知有独立流程。
///
/// - **OPEN**: 对外开放认证 — 对接方通过 DaxPay 获取用户标识(重定向模式, 非 JSON 返回)。
///   类似海科融通 getOpenid 接口: 对接方上送 redirect_url, 系统完成 OAuth 后重定向回去带 openid。
@Getter
@RequiredArgsConstructor
public enum AuthScene {

    /// 支付认证: 为完成支付获取用户标识
    PAYMENT("payment"),

    /// 平台自用认证: 系统功能自用(调试/通知/社交登录)
    PLATFORM("platform"),

    /// 对外开放认证: 对接方获取用户标识(重定向接口)
    OPEN("open");

    private final String code;
}
