package cn.daxpay.multi.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 退款同步状态枚举
 * @author xxm
 * @since 2024/1/29
 */
@Getter
@AllArgsConstructor
public enum RefundSyncResultEnum {

    SYNC_FAIL("sync_fail","退款查询失败"),
    FAIL("fail","退款失败"),
    SUCCESS("success","退款成功"),
    CLOSE("close","退款关闭"),
    PROGRESS("progress","退款中"),
    NOT_FOUND("not_found", "交易不存在"),
    UNKNOWN("unknown","状态未知"),
    ;

    /** 编码 */
    private final String code;
    /** 名称 */
    private final String name;
}
