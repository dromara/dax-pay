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
    SYNC_FAIL("sync_fail", "支付查询失败"),
    FAIL("fail", "支付失败"),
    SUCCESS("success", "支付成功"),
    PROGRESS("progress", "支付中"),
    CLOSED("closed", "支付已关闭"),
    REFUND("refund", "支付退款"),
    NOT_FOUND("found", "交易不存在"),
    /** 订单到了超时时间, 被手动设置订单为这个状态 */
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
