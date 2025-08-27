package org.dromara.daxpay.core.enums;

import org.dromara.daxpay.core.exception.ConfigNotExistException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 网关支付调起方式
 * 字典: gateway_call_type
 * @author xxm
 * @since 2024/11/27
 */
@Getter
@AllArgsConstructor
public enum GatewayCallTypeEnum {

    QR_CODE("qr_code", "扫码支付"),
    BAR_CODE("bar_code", "付款码支付"),
    LINK("link", "跳转链接"),
    MINI_APP("mini_app", "小程序"),
    APP("app", "APP支付"),
    JSAPI("jsapi", "JSAPI"),
    FROM("from", "表单方式"),
    ;

    private final String code;
    private final String name;

    public static GatewayCallTypeEnum findBuyCode(String code){
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new ConfigNotExistException("不支持的收银台类型"));

    }
}
