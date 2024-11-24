package org.dromara.daxpay.single.sdk.code;

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

    TRANSFERRING("transferring", "转账中"),
    SUCCESS("success", "转账成功"),
    FAIL("fail", "转账失败"),
    ;

    private final String code;
    private final String name;

}
