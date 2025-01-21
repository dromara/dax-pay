package org.dromara.daxpay.single.sdk.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 支付同步结果
 *
 * @author xxm
 * @since 2021/4/21
 */
@Getter
@AllArgsConstructor
public enum PaySyncStatusEnum {
    FAIL("pay_fail", "支付查询失败"),
    SUCCESS("pay_success", "支付成功"),
    PROGRESS("pay_progress", "支付中"),
    CLOSED("pay_closed", "支付已关闭"),
    REFUND("pay_refund", "支付退款"),
    NOT_FOUND("pay_not_found", "交易不存在"),
    /**
     * 未查询到订单(具体类型未知)
     * 区别于上面的未查询到订单，有些支付方式如支付宝，发起支付后并不能查询到订单，需要用户进行操作后才能查询到订单，
     * 所以查询为了区分，增加一个未知的状态, 用于处理这种特殊情况, 然后根据业务需要，关闭订单或者进行其他操作
     */
    NOT_FOUND_UNKNOWN("pay_not_found_unknown","交易不存在(特殊)"),
    /** 不属于网关同步过来的状态, 需要手动设置, 处理本地订单到了超时时间, 但是网关和本地都未关闭, 需要触发关闭相关处理 */
    TIMEOUT("pay_timeout", "支付超时");

    /** 编码 */
    private final String code;
    /** 名称 */
    private final String name;

    public static PaySyncStatusEnum getByCode(String code) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("该枚举不存在"));
    }
}
