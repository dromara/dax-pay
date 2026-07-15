package cn.daxpay.open.plugin.easypay.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/// # 易支付 API 版本
///
@Getter
@RequiredArgsConstructor
public enum EasyPayApiVersionEnum {
    V1("v1"),
    V2("v2");

    private final String code;

    public static EasyPayApiVersionEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.code, code))
                .findFirst()
                .orElse(null);
    }
}
