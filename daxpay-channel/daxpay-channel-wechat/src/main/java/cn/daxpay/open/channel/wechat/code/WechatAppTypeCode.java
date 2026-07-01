package cn.daxpay.open.channel.wechat.code;

import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;

import java.util.Map;

/// # 微信应用类型与支付能力映射
///
/// 定义微信应用类型([cn.daxpay.open.channel.wechat.entity.isv.WechatIsvApp].appType)常量，
/// 以及支付能力(PayCapabilityEnum)到应用类型的默认推导关系。
///
/// 当未显式配置「能力→应用」关联时，[cn.daxpay.open.channel.wechat.service.isv.WechatIsvAppCapabilityService]
/// 会依据本映射按能力自动推导出合适的 appType，进而在应用列表中匹配对应应用。
///
public final class WechatAppTypeCode {

    private WechatAppTypeCode() {
    }

    /// 应用类型: 公众号
    public static final String OFFICIAL_ACCOUNT = "official_account";

    /// 应用类型: 小程序
    public static final String MINI_PROGRAM = "mini_program";

    /// 应用类型: 移动应用
    public static final String MOBILE_APP = "mobile_app";

    /// 支付能力 → 应用类型的默认推导映射
    ///
    /// - JSAPI支付 → 公众号
    /// - 小程序支付 → 小程序
    /// - APP支付 → 移动应用
    /// - 扫码/H5/付款码 → 公众号(微信扫码/H5/付款码通常使用公众号应用)
    private static final Map<String, String> CAPABILITY_APP_TYPE_MAP = Map.of(
            PayCapabilityEnum.WECHAT_JSAPI.getCode(), OFFICIAL_ACCOUNT,
            PayCapabilityEnum.WECHAT_QR.getCode(), OFFICIAL_ACCOUNT,
            PayCapabilityEnum.WECHAT_H5.getCode(), OFFICIAL_ACCOUNT,
            PayCapabilityEnum.WECHAT_BARCODE.getCode(), OFFICIAL_ACCOUNT,
            PayCapabilityEnum.WECHAT_MINI.getCode(), MINI_PROGRAM,
            PayCapabilityEnum.WECHAT_APP.getCode(), MOBILE_APP);

    /// 根据支付能力编码推导默认应用类型；未知能力返回 null
    public static String resolveAppType(String capabilityCode) {
        if (capabilityCode == null) {
            return null;
        }
        return CAPABILITY_APP_TYPE_MAP.get(capabilityCode);
    }
}
