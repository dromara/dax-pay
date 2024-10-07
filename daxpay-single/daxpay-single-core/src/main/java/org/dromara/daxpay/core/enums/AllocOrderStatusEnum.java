package org.dromara.daxpay.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 分账状态枚举
 * @author xxm
 * @since 2024/4/7
 */
@Getter
@AllArgsConstructor
public enum AllocOrderStatusEnum {

    ALLOC_PROCESSING("alloc_processing", "分账处理中"),
    ALLOC_END("alloc_end", "分账完成"),
    ALLOC_FAILED("alloc_failed", "分账失败"),
    FINISH("finish", "完结"),
    FINISH_FAILED("finish_failed", "完结失败"),
    ;

    final String code;
    final String name;

}
