package org.dromara.daxpay.sdk.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 网关支付类型
 * 字典 gateway_pay_type
 * @author xxm
 * @since 2024/11/26
 */
@Getter
@AllArgsConstructor
public enum GatewayPayTypeEnum {

    /** H5收银台支付 */
    H5("h5"),
    /** PC收银台支付 */
    PC("pc"),
    /** 小程序收银台支付 */
    MINI_APP("mini_app"),
    /** 网关聚合支付 */
    AGGREGATE("aggregate"),
    ;

    private final String code;
}
