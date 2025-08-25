package org.dromara.daxpay.channel.alipay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 代商户操作事务状态
 * @author xxm
 * @since 2024/11/5
 */
@Getter
@AllArgsConstructor
public enum AlipayBatchStatusEnum {

    /** 初始状态 */
    INIT("init", "初始状态"),
    /**提交状态  */
    SUBMIT("submit","提交状态"),
    ;
    private final String code;
    private final String name;
}
