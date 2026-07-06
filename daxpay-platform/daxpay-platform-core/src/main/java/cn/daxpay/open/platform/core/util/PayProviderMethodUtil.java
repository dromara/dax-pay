package cn.daxpay.open.platform.core.util;

import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.model.PayProviderMethodEntry;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum.*;

/// # 支付渠道支付方式目录（平台 V1）
///
/// 全平台固定的「支付渠道 × 支付方式」组合，供路由配置、下单校验、管理端展示复用。
/// 与 PayMethodEnum、PayProviderEnum 等枚举类区分，本类为静态目录定义，不放在 enums 包下。
@Deprecated
@UtilityClass
public class PayProviderMethodUtil {

    /// 各支付渠道下固定、有序的支付方式列表
    private static final Map<PayProviderEnum, List<PayMethodEnum>> PROVIDER_METHODS;

    /// 扁平化目录项，顺序与渠道内列表一致
    private static final List<PayProviderMethodEntry> ALL_ENTRIES;

    static {
        Map<PayProviderEnum, List<PayMethodEnum>> providerMethods = new EnumMap<>(PayProviderEnum.class);
        // 聚合支付：扫码 / 付款码
        providerMethods.put(PayProviderEnum.AGGREGATE_PAY, List.of(
                AGGREGATE_PAY_QRCODE,
                AGGREGATE_PAY_BARCODE
        ));
        // 微信：JSAPI / APP / H5 / Native / 小程序 / 付款码 / 小程序收银台
        providerMethods.put(PayProviderEnum.WECHAT, List.of(
                WECHAT_JSAPI,
                WECHAT_APP,
                WECHAT_H5,
                WECHAT_QR,
                WECHAT_MINI,
                WECHAT_BARCODE,
                WECHAT_CASHIER
        ));
        // 支付宝：付款码 / 扫码 / APP / 手机网站 / 电脑网站 / JSAPI
        providerMethods.put(PayProviderEnum.ALIPAY, List.of(
                ALIPAY_BARCODE,
                ALIPAY_QR,
                ALIPAY_APP,
                ALIPAY_H5,
                ALIPAY_PC,
                ALIPAY_JSAPI
        ));
        // 银联：扫码 / 付款码 / H5 / JSAPI
        providerMethods.put(PayProviderEnum.UNION_PAY, List.of(
                UNION_QR,
                UNION_PAY_BARCODE,
                UNION_H5,
                UNION_JSAPI
        ));
        // Visa：网关 / 刷卡
        providerMethods.put(PayProviderEnum.VISA, List.of(
                VISA_CARD_GATEWAY,
                VISA_CARD_PRESENT
        ));
        // 万事达：网关 / 刷卡
        providerMethods.put(PayProviderEnum.MASTERCARD, List.of(
                MASTERCARD_CARD_GATEWAY,
                MASTERCARD_CARD_PRESENT
        ));
        PROVIDER_METHODS = Collections.unmodifiableMap(copyProviderMethods(providerMethods));

        List<PayProviderMethodEntry> entries = new ArrayList<>();
        for (Map.Entry<PayProviderEnum, List<PayMethodEnum>> entry : PROVIDER_METHODS.entrySet()) {
            for (PayMethodEnum method : entry.getValue()) {
                entries.add(new PayProviderMethodEntry(entry.getKey(), method));
            }
        }
        ALL_ENTRIES = List.copyOf(entries);
    }

    /// 深拷贝渠道-支付方式映射，用于保护静态常量的不可变性
    private static Map<PayProviderEnum, List<PayMethodEnum>> copyProviderMethods(
            Map<PayProviderEnum, List<PayMethodEnum>> source) {
        Map<PayProviderEnum, List<PayMethodEnum>> copy = new EnumMap<>(PayProviderEnum.class);
        source.forEach((provider, methods) -> copy.put(provider, List.copyOf(methods)));
        return copy;
    }

    /// 指定支付渠道下的固定支付方式（有序）
    public List<PayMethodEnum> methodsOf(PayProviderEnum provider) {
        if (provider == null) {
            return List.of();
        }
        return PROVIDER_METHODS.getOrDefault(provider, List.of());
    }

    /// 指定支付渠道编码下的固定支付方式（有序）
    public List<PayMethodEnum> methodsOf(String providerCode) {
        return methodsOf(PayProviderEnum.findByCode(providerCode));
    }

    /// 是否属于渠道支付方式目录中的 (provider, method) 组合
    public boolean contains(PayProviderEnum provider, String methodCode) {
        if (provider == null || methodCode == null || methodCode.isBlank()) {
            return false;
        }
        return methodsOf(provider).stream().anyMatch(method -> Objects.equals(method.getCode(), methodCode));
    }

    /// 是否属于渠道支付方式目录中的 (provider, method) 组合
    public boolean contains(String providerCode, String methodCode) {
        return contains(PayProviderEnum.findByCode(providerCode), methodCode);
    }

    /// 渠道支付方式目录全量项（以本类静态定义为准，运行时启用态见 `PayProviderService`）
    public List<PayProviderMethodEntry> allEntries() {
        return ALL_ENTRIES;
    }

    /// 是否必须显式传入支付渠道（仅 other 无法推导）
    public boolean requiresProviderForMethod(String methodCode) {
        return Objects.equals(PayMethodEnum.OTHER.getCode(), methodCode);
    }
}
