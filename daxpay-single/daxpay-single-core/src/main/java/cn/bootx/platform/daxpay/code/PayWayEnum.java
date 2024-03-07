package cn.bootx.platform.daxpay.code;

import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 支付方式
 *
 * @author xxm
 * @since 2021/7/26
 */
@Getter
@AllArgsConstructor
public enum PayWayEnum {

    NORMAL("normal", "常规支付"),
    WAP("wap", "wap支付"),
    APP("app", "应用支付"),
    WEB("web", "web支付"),
    QRCODE("qrcode", "扫码支付"),
    BARCODE("barcode", "付款码"),
    // 通用
    JSAPI("jsapi", "公众号/小程序支付"),
    // 在非支付宝和微信中, 但支持这两类支付的时候, 需要进行区分
    JSAPI_WX_PAY("jsapi_wx_pay", "微信JS支付"),
    JSAPI_ALI_PAY("jsapi_ali_pay", "支付宝JS支付");

    /** 编码 */
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
