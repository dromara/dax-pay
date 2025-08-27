package org.dromara.daxpay.core.enums;

import org.dromara.daxpay.core.exception.UnsupportedAbilityException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 支付方式
 * 字典: pay_method
 * @author xxm
 * @since 2021/7/26
 */
@Getter
@AllArgsConstructor
public enum PayMethodEnum {

    WAP("wap", "H5支付"),
    APP("app",  "应用支付"),
    WEB("web", "PC支付"),
    QRCODE("qrcode",  "扫码支付"),
    BARCODE("barcode", "付款码支付"),
    JSAPI("jsapi", "Jsapi支付"),
    OTHER("other",  "其他支付方式"),
    ;

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
                .orElseThrow(() -> new UnsupportedAbilityException("该支付方式不存在"));
    }
}
