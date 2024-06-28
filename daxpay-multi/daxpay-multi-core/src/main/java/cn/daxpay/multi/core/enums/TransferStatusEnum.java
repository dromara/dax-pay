package cn.daxpay.multi.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 转账状态
 * @author xxm
 * @since 2024/6/11
 */
@Getter
@AllArgsConstructor
public enum TransferStatusEnum {

    /** 转账中 */
    TRANSFERRING("transferring"),
    /** 转账成功 */
    SUCCESS("success"),
    /** 转账失败 */
    FAIL("fail"),
    ;

    private final String code;

}
