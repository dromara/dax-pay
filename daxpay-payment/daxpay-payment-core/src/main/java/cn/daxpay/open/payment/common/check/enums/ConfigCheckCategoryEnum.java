package cn.daxpay.open.payment.common.check.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 配置检查分类
///
/// 标识一项"配置是否已完成"的维度, 运营端与商户端共用。
/// `code` 同时作为前端 i18n key 后缀(`configCheck.category.{code}`)。
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
