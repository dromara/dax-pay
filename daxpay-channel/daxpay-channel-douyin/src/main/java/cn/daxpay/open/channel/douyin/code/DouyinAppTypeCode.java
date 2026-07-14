package cn.daxpay.open.channel.douyin.code;

import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;

import java.util.Map;

/// # 抖音应用类型与支付能力映射
///
/// 定义抖音直连商户应用([cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectApp#appType])的常量，
/// 以及支付能力(PayCapabilityEnum)到应用类型的默认推导关系。
///
/// 当未显式配置「能力→应用」关联时，[cn.daxpay.open.channel.douyin.service.direct.DouyinDirectAppCapabilityService]
/// 会依据本映射按能力自动推导出合适的 appType，进而在应用列表中匹配对应应用。
///
public final class DouyinAppTypeCode {

    private DouyinAppTypeCode() {
    }

    /// 应用类型: 小程序
    public static final String MINI_PROGRAM = "mini_program";

    /// 应用类型: 移动应用
    public static final String MOBILE_APP = "mobile_app";

    /// 应用类型: 网站应用
    public static final String WEB_APP = "web_app";

    /// 支付能力 → 应用类型的默认推导映射
    ///
    /// - 小程序支付 → 小程序
    /// - APP支付 → 移动应用
    /// - 扫码/H5 → 网站应用
    private static final Map<String, String> CAPABILITY_APP_TYPE_MAP = Map.of(
            PayCapabilityEnum.DOUYIN_JSAPI.getCode(), MINI_PROGRAM,
            PayCapabilityEnum.DOUYIN_APP.getCode(), MOBILE_APP,
            PayCapabilityEnum.DOUYIN_QR.getCode(), WEB_APP,
            PayCapabilityEnum.DOUYIN_H5.getCode(), WEB_APP);

    /// 根据支付能力编码推导默认应用类型；未知能力返回 null
    public static String resolveAppType(String capabilityCode) {
        if (capabilityCode == null) {
            return null;
        }
        return CAPABILITY_APP_TYPE_MAP.get(capabilityCode);
    }
}
