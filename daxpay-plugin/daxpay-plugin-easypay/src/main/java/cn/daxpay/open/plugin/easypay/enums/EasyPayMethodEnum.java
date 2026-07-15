package cn.daxpay.open.plugin.easypay.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/// # 易支付协议支付方式
///
@Getter
@RequiredArgsConstructor
public enum EasyPayMethodEnum {
    ALIPAY("alipay"),
    WECHAT("wxpay"),
    AGGREGATE("aggregate");

    private final String code;

    public static EasyPayMethodEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.code, code))
                .findFirst()
                .orElse(null);
    }
}
