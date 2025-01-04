package org.dromara.daxpay.single.sdk.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 分账明细处理结果
 * 字典: alloc_detail_result
 * @author xxm
 * @since 2024/4/16
 */
@Getter
@AllArgsConstructor
public enum AllocDetailResultEnum {

    PENDING("pending", "待分账"),
    SUCCESS("success", "分账成功"),
    FAIL("fail", "分账失败"),
    /** 金额为0时不进行分账 */
    IGNORE("ignore", "忽略分账"),
    ;

    private final String code;
    private final String name;
}
