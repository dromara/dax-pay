package cn.bootx.platform.daxpay.code.pay;

import cn.bootx.platform.daxpay.exception.payment.PayFailureException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 支付方式
 *
 * @author xxm
 * @date 2021/7/26
 */
@Getter
@AllArgsConstructor
public enum PayWayEnum {

    NORMAL( "NORMAL", "常规支付"),
    WAP("WAP", "wap支付"),
    APP( "APP", "应用支付"),
    WEB("WEB", "web支付"),
    QRCODE( "QRCODE", "扫码支付"),
    BARCODE("BARCODE", "付款码"),
    JSAPI( "JSAPI", "公众号/小程序支付");


    /** 支付方式字符编码 */
    private final String code;

    /** 名称 */
    private final String name;

    /**
     * 根据字符编码获取
     */
    public static PayWayEnum findByCode(String code) {
        return Arrays.stream(PayWayEnum.values())
            .filter(e -> Objects.equals(code, e.getCode()))
            .findFirst()
            .orElseThrow(() -> new PayFailureException("不存在的支付方式"));
    }

}
