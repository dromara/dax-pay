package cn.daxpay.open.channel.alipay.code;

import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;

import java.util.List;
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

    /// 支付能力 → 兼容应用类型集合(有序, 首个为兜底推导优先级)
    ///
    /// - JSAPI → 小程序(场景强绑 buyer_id)
    /// - APP支付 → 移动应用
    /// - 付款码/扫码/H5/PC → 网站应用优先, 兼容小程序/移动应用(不依赖 buyer_id)
    private static final Map<String, List<String>> CAPABILITY_APP_TYPE_MAP = Map.of(
            PayCapabilityEnum.ALIPAY_JSAPI.getCode(), List.of(MINI_PROGRAM),
            PayCapabilityEnum.ALIPAY_APP.getCode(), List.of(MOBILE_APP),
            PayCapabilityEnum.ALIPAY_BARCODE.getCode(), List.of(WEB_APP, MINI_PROGRAM, MOBILE_APP),
            PayCapabilityEnum.ALIPAY_QR.getCode(), List.of(WEB_APP, MINI_PROGRAM, MOBILE_APP),
            PayCapabilityEnum.ALIPAY_H5.getCode(), List.of(WEB_APP, MINI_PROGRAM, MOBILE_APP),
            PayCapabilityEnum.ALIPAY_PC.getCode(), List.of(WEB_APP, MINI_PROGRAM, MOBILE_APP));

    /// 根据支付能力推导全部兼容应用类型(有序, 首个为兜底优先级)；未知能力返回空列表
    public static List<String> resolveCompatibleAppTypes(String capabilityCode) {
        if (capabilityCode == null) {
            return List.of();
        }
        return CAPABILITY_APP_TYPE_MAP.getOrDefault(capabilityCode, List.of());
    }

    /// 根据支付能力编码推导默认(优先级最高)应用类型；未知能力返回 null
    public static String resolveAppType(String capabilityCode) {
        List<String> types = resolveCompatibleAppTypes(capabilityCode);
        return types.isEmpty() ? null : types.getFirst();
    }

    /// 校验应用类型与支付能力是否兼容；未知能力不做强制约束
    public static boolean isCompatible(String appType, String capability) {
        List<String> types = resolveCompatibleAppTypes(capability);
        if (types.isEmpty()) {
            return true;
        }
        return types.contains(appType);
    }
}
