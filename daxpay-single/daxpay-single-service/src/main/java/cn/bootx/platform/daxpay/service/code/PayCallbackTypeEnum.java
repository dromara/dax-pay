package cn.bootx.platform.daxpay.service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 回调类型枚举
 * @author xxm
 * @since 2024/1/24
 */
@Getter
@AllArgsConstructor
public enum PayCallbackTypeEnum {

    PAY("pay", "支付回调"),
    REFUND("refund", "退款回调");

    public final String code;
    public final String name;
}
