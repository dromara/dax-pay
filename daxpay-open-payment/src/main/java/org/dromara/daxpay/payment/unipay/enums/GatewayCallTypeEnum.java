package org.dromara.daxpay.payment.unipay.enums;

import org.dromara.daxpay.payment.common.exception.ConfigNotExistException;
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
    LINK("link", "跳转链接"),
    MINI_APP("mini_app", "小程序"),
    JSAPI("jsapi", "JSAPI"),
    FROM("from", "表单方式"),
    ;

    private final String code;
    private final String name;

    public static GatewayCallTypeEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new ConfigNotExistException("不支持的收银台类型"));

    }
}
