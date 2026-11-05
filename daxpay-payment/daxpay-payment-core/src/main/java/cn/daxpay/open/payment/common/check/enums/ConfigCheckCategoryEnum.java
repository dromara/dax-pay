package cn.daxpay.open.payment.common.check.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 配置检查分类
///
/// 标识一项"配置是否已完成"的维度, 运营端与商户端共用。
/// `code` 同时作为前端 i18n key 前缀, checker 下发 `configCheck.{code}.title` / `configCheck.{code}.description`。
@Getter
@RequiredArgsConstructor
public enum ConfigCheckCategoryEnum {

    // ===== 运营端(平台级)配置 =====
    /// 平台站点信息
    PLATFORM_WEBSITE("platformWebsite"),
    /// 平台访问地址
    PLATFORM_URL("platformUrl"),
    /// 对象存储
    PLATFORM_OSS("platformOss"),
    /// 社交登录配置
    SOCIAL_LOGIN("socialLogin"),
    /// 平台 MQTT 接入
    PLATFORM_MQTT("platformMqtt"),
    /// 平台邮件服务
    PLATFORM_MAIL("platformMail"),
    /// 平台支付宝应用凭据
    PLATFORM_ALIPAY_AUTH("platformAlipayAuth"),
    /// 平台微信公众号凭据
    PLATFORM_WECHAT_MP("platformWechatMp"),
    /// 收银台小程序应用
    PLATFORM_MOBILE_APP("platformMobileApp"),

    // ===== 商户端配置 =====
    /// 商户应用
    MCH_APP("mchApp"),
    /// API 凭证
    MCH_CREDENTIAL("mchCredential"),
    /// 通道商户
    CHANNEL_MERCHANT("channelMerchant"),
    /// 支付路由
    PAY_ROUTE("payRoute"),
    /// 通知配置
    MCH_NOTIFY("mchNotify");

    private final String code;
}
