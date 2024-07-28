package cn.daxpay.multi.service.enums;

import cn.daxpay.multi.core.exception.TradeStatusErrorException;
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
public enum PaySyncResultEnum {
    FAIL("fail", "支付查询失败"),
    SUCCESS("success", "支付成功"),
    PROGRESS("progress", "支付中"),
    CLOSED("closed", "支付已关闭"),
    REFUND("refund", "支付退款"),
    NOT_FOUND("found", "交易不存在"),
    /**
     * 具体类型未知, 有些支付方式如支付宝，发起支付后并不能查询到订单，需要用户进行操作后才能查询到订单，
     * 所以增加一个未知的状态, 用于处理这类特殊情况, 可以视作支付中状态
     */
    UNKNOWN("unknown","状态未知"),
    /** 订单到了超时时间, 被手动设置订单为次状态 */
    TIMEOUT("timeout", "支付超时");

    /** 编码 */
    private final String code;
    /** 名称 */
    private final String name;

    public static PaySyncResultEnum getByCode(String code) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new TradeStatusErrorException("不存在的支付状态"));
    }
}
