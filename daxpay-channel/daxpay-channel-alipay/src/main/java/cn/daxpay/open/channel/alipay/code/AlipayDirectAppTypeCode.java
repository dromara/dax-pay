package cn.daxpay.open.channel.alipay.code;

import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;

import java.util.Map;

/// # 支付宝直连应用类型与支付能力映射
///
/// 定义支付宝直连商户应用类型([AlipayDirectApp#appType])常量，
/// 以及支付能力(PayCapabilityEnum)到应用类型的默认推导关系。
///
/// 当通道商户未显式配置「能力→应用」关联时，[AlipayDirectConfigAssembler] 会依据本映射
/// 按能力自动推导出合适的 appType，进而在该商户的应用列表中匹配对应应用。
///
public final class AlipayDirectAppTypeCode {

    private AlipayDirectAppTypeCode() {
    }

    /// 应用类型: 小程序应用
    public static final String MINI_PROGRAM = "mini_program";

    /// 应用类型: 移动应用
    public static final String MOBILE_APP = "mobile_app";

    /// 应用类型: 网站应用
    public static final String WEB_APP = "web_app";

    /// 支付能力 → 应用类型的默认推导映射
    ///
    /// - JSAPI 支付(含小程序) → 小程序应用(官方 JSAPI_PAY 主场景)
    /// - APP支付 → 移动应用
    /// - 其余支付宝能力(付款码/扫码/H5/PC) → 网站应用
    private static final Map<String, String> CAPABILITY_APP_TYPE_MAP = Map.of(
            PayCapabilityEnum.ALIPAY_JSAPI.getCode(), MINI_PROGRAM,
            PayCapabilityEnum.ALIPAY_APP.getCode(), MOBILE_APP,
            PayCapabilityEnum.ALIPAY_BARCODE.getCode(), WEB_APP,
            PayCapabilityEnum.ALIPAY_QR.getCode(), WEB_APP,
            PayCapabilityEnum.ALIPAY_H5.getCode(), WEB_APP,
            PayCapabilityEnum.ALIPAY_PC.getCode(), WEB_APP);

    /// 根据支付能力编码推导默认应用类型；未知能力返回 null
    public static String resolveAppType(String capabilityCode) {
        if (capabilityCode == null) {
            return null;
        }
        return CAPABILITY_APP_TYPE_MAP.get(capabilityCode);
    }

    /// 校验应用类型与支付能力是否兼容；未知能力不做强制约束
    public static boolean isCompatible(String appType, String capability) {
        String expected = resolveAppType(capability);
        if (expected == null) {
            return true;
        }
        return expected.equals(appType);
    }
}
