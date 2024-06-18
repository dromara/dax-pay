package cn.daxpay.single.core.code;

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

    ALLOCATION_PROCESSING("allocation_processing", "分账处理中"),
    ALLOCATION_END("allocation_end", "分账完成"),
    ALLOCATION_FAILED("allocation_failed", "分账失败"),
    FINISH("finish", "完结"),
    FINISH_FAILED("finish_failed", "完结失败"),
    /** 忽略, 如果金额为0, 不进行分账 */
    IGNORE("ignore", "忽略"),
    ;

    final String code;
    final String name;

}
