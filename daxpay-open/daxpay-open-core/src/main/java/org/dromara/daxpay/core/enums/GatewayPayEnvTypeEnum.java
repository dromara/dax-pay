package org.dromara.daxpay.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 网关支付时所处的环境, 如H5浏览器, 微信浏览器, 支付宝浏览器等
 * @author xxm
 * @since 2025/3/29
 */
@Getter
@AllArgsConstructor
public enum GatewayPayEnvTypeEnum {
    H5("h5", "H5浏览器"),
    PC("pc", "PC浏览器"),
    WECHAT("wechat", "微信环境"),
    ALIPAY("alipay", "支付宝环境"),
    UNION_PAY("union_pay", "云闪付环境"),
    ;

    private final String code;
    private final String name;


}
