package org.dromara.daxpay.sdk.code;

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


    /** wap支付 */
    WAP("wap"),
    /** 应用支付 */
    APP("app"),
    /** web支付 */
    WEB("web"),
    /** 扫码支付 */
    QRCODE("qrcode"),
    /** 付款码 */
    BARCODE("barcode"),
    /** 小程序支付 */
    JSAPI("jsapi"),
    /** 其他支付 */
    OTHER("other"),
    ;

    /** 编码 */
    private final String code;
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
