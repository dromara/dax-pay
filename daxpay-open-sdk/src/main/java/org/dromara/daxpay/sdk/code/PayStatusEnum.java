package org.dromara.daxpay.sdk.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 支付状态
 * @author xxm
 * @since 2023/12/17
 */
@Getter
@RequiredArgsConstructor
public enum PayStatusEnum {
    /** 未指定通道和支付方式等信息, 通常只有网关支付方式创建的订单 */
    WAIT("wait"),
    /** 支付中 */
    PROGRESS("progress"),
    /** 成功 */
    SUCCESS("success"),
    /** 支付关闭 */
    CLOSE("close"),
    /** 退款中 */
    REFUNDING("refunding"),
    /** 部分退款 */
    PARTIAL_REFUND("partial_refund"),
    /** 全部退款 */
    REFUNDED("refunded"),
    /** 失败 */
    FAIL("fail"),
    /** 支付超时, 订单到了超时时间, 被手动设置订单为这个状态, 通常作为中间流转状态，可以视为支付关闭 */
    TIMEOUT("timeout"),
    ;

    /** 编码 */
    private final String code;

}
