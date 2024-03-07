package cn.bootx.platform.daxpay.service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 云闪付
 * @author xxm
 * @since 2024/3/7
 */
@Getter
@AllArgsConstructor
public enum UnionPayRecordTypeEnum {

    /** 支付 */
    PAY("pay", "支付"),
    /** 退款 */
    REFUND("refund", "退款");

    private final String code;
    private final String name;
}
