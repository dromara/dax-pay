package cn.bootx.platform.daxpay.service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 钱包记录类型
 * @author xxm
 * @since 2024/2/18
 */
@Getter
@AllArgsConstructor
public enum WalletRecordTypeEnum {

    /** 创建 */
    CREATE("create", "创建"),

    /** 支付 */
    PAY("pay", "支付"),

    /** 退款 */
    REFUND("refund", "退款"),

    /** 支付关闭 */
    CLOSE_PAY("close_pay", "支付关闭"),

    /** 退款关闭 */
    CLOSE_REFUND("close_refund", "退款关闭");

    private final String code;
    private final String name;
}
