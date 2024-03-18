package cn.bootx.platform.daxpay.sdk.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 聚合支付相关枚举
 * @author xxm
 * @since 2024/2/9
 */
@Getter
@AllArgsConstructor
public enum AggregatePayEnum {

    UA_ALI_PAY("Alipay", "支付宝"),
    UA_WECHAT_PAY("MicroMessenger", "微信支付"),
    UA_UNION_PAY("CloudPay", "云闪付");

    /** 支付渠道字符编码 */
    private final String code;

    /** 名称 */
    private final String name;
}
