package org.dromara.daxpay.single.sdk.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 支付方式
 * @author xxm
 * @since 2021/7/26
 */
@Getter
@AllArgsConstructor
public enum PayMethodEnum {

    WAP("wap", "wap支付"),
    APP("app", "应用支付"),
    WEB("web", "web支付"),
    QRCODE("qrcode", "扫码支付"),
    BARCODE("barcode", "付款码"),
    JSAPI("jsapi", "公众号/小程序支付");

    /** 编码 */
    private final String code;
    /** 名称 */
    private final String name;
    /**
     * 根据编码获取枚举
     */
    public static PayMethodEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(o -> Objects.equals(o.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("该枚举不存在"));
    }
}
