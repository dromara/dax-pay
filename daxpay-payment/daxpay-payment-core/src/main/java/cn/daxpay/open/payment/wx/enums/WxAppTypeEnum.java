package cn.daxpay.open.payment.wx.enums;

import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/// # 微信开放应用类型
///
/// 公众号 / 小程序 / 移动应用；创建后不可改。
///
@Getter
@RequiredArgsConstructor
public enum WxAppTypeEnum implements I18nSupport {
    /// 公众号
    OFFICIAL_ACCOUNT("official_account"),
    /// 小程序
    MINI_PROGRAM("mini_program"),
    /// 移动应用
    MOBILE_APP("mobile_app"),
    ;

    private final String code;

    /// 支付能力 → 应用类型的默认推导映射
    ///
    /// - JSAPI/扫码/H5/付款码/收银台 → 公众号
    /// - 小程序支付 → 小程序
    /// - APP支付 → 移动应用
    private static final Map<String, String> CAPABILITY_APP_TYPE_MAP = Map.of(
            PayCapabilityEnum.WECHAT_JSAPI.getCode(), OFFICIAL_ACCOUNT.code,
            PayCapabilityEnum.WECHAT_QR.getCode(), OFFICIAL_ACCOUNT.code,
            PayCapabilityEnum.WECHAT_H5.getCode(), OFFICIAL_ACCOUNT.code,
            PayCapabilityEnum.WECHAT_BARCODE.getCode(), OFFICIAL_ACCOUNT.code,
            PayCapabilityEnum.WECHAT_CASHIER.getCode(), OFFICIAL_ACCOUNT.code,
            PayCapabilityEnum.WECHAT_MINI.getCode(), MINI_PROGRAM.code,
            PayCapabilityEnum.WECHAT_APP.getCode(), MOBILE_APP.code);

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.wx_app_type";
    }

    /// 根据编码获取枚举；未知编码返回 null
    public static WxAppTypeEnum findByCode(String code) {
        if (code == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.code, code))
                .findFirst()
                .orElse(null);
    }

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
