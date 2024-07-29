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
public enum TransferSyncResultEnum {
    SYNC_FAIL("sync_fail","同步失败"),
    SUCCESS("success","转账成功"),
    PROGRESS("progress","转账中"),
    CLOSE("close","转账关闭"),
    FAIL("fail","转账失败"),
    NOT_FOUND("not_found", "转账不存在"),
    UNKNOWN("unknown","状态未知"),
    ;
    private final String code;
    private final String name;
}
