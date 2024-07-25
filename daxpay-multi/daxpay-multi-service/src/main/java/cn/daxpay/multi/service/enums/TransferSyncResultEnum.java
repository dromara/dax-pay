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
    FAIL("transfer_fail","转账失败"),
    NOT_FOUND("transfer_not_found", "转账不存在"),
    PROGRESS("transfer_progress","转账中"),
    SUCCESS("transfer_success","转账成功")
    ;
    private final String code;
    private final String name;
}
