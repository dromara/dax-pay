package cn.bootx.platform.daxpay.code;

import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 支付同步状态
 *
 * @author xxm
 * @since 2021/4/21
 */
@Getter
@AllArgsConstructor
public enum PaySyncStatusEnum {
    FAIL("fail", "查询失败"),
    PAY_SUCCESS("pay_success", "支付成功"),
    PAY_WAIT("pay_wait", "待支付"),
    CLOSED("closed", "已关闭"),
    REFUND("refund", "已退款"),
    NOT_FOUND("not_found", "交易不存在"),
    /**
     * 未查询到订单(具体类型未知)
     * 区别于上面的未查询到订单，有些支付方式如支付宝，发起支付后并不能查询到订单，需要用户进行操作后才能查询到订单，
     * 所以查询为了区分，增加一个未知的状态, 用于处理这种特殊情况, 然后根据业务需要，关闭订单或者进行其他操作
     */
    NOT_FOUND_UNKNOWN("not_found_unknown","交易不存在(特殊)"),
    /** 不属于网关同步过来的状态, 需要手动设置, 处理本地订单到了超时时间, 但是网关和本地都未关闭, 需要触发关闭相关处理 */
    TIMEOUT("timeout", "超时未关闭");

    /** 编码 */
    private final String code;
    /** 名称 */
    private final String name;

    public static PaySyncStatusEnum getByCode(String code) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new PayFailureException("不存在的支付同步状态"));
    }
}
