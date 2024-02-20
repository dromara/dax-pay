package cn.bootx.platform.daxpay.service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付宝流水记录类型
 * @author xxm
 * @since 2024/2/20
 */
@Getter
@AllArgsConstructor
public enum AliPayRecordTypeEnum {

    /** 支付 */
    PAY("pay", "支付")
    ,
    /** 退款 */
    REFUND("refund", "退款");

    private final String code;
    private final String name;
}
