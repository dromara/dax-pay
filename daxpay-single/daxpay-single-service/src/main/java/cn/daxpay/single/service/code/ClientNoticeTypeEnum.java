package cn.daxpay.single.service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息通知类型
 * @author xxm
 * @since 2024/2/20
 */
@Getter
@AllArgsConstructor
public enum ClientNoticeTypeEnum {
    PAY("pay", "支付通知"),
    REFUND("refund", "退款通知");

    private final String type;
    private final String name;
}
