package cn.daxpay.open.payment.douyin.enums;

import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/// # 抖音开放应用类型
///
/// 小程序 / 移动应用 / 网站应用；创建后不可改。
///
@Getter
@RequiredArgsConstructor
public enum DyAppTypeEnum implements I18nSupport {
    /// 小程序
    MINI_PROGRAM("mini_program"),
    /// 移动应用
    MOBILE_APP("mobile_app"),
    /// 网站应用
    WEB_APP("web_app"),
    ;

    private final String code;

    /// 支付能力 → 兼容应用类型集合(有序, 首个为兜底推导优先级)
    ///
    /// - JSAPI → 小程序(场景强绑 openId)
    /// - APP支付 → 移动应用
    /// - 扫码/H5 → 网站应用优先, 兼容小程序/移动应用(不依赖 openId)
    private static final Map<String, List<String>> CAPABILITY_APP_TYPE_MAP = Map.of(
            PayCapabilityEnum.DOUYIN_JSAPI.getCode(), List.of(MINI_PROGRAM.code),
            PayCapabilityEnum.DOUYIN_APP.getCode(), List.of(MOBILE_APP.code),
            PayCapabilityEnum.DOUYIN_QR.getCode(), List.of(WEB_APP.code, MINI_PROGRAM.code, MOBILE_APP.code),
            PayCapabilityEnum.DOUYIN_H5.getCode(), List.of(WEB_APP.code, MINI_PROGRAM.code, MOBILE_APP.code));

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.dy_app_type";
    }

    /// 根据编码获取枚举；未知编码返回 null
    public static DyAppTypeEnum findByCode(String code) {
        if (code == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.code, code))
                .findFirst()
                .orElse(null);
    }

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
