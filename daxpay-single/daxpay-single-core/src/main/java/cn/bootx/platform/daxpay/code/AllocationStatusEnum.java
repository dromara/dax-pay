package cn.bootx.platform.daxpay.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 分账状态枚举
 * @author xxm
 * @since 2024/4/7
 */
@Getter
@AllArgsConstructor
public enum AllocationStatusEnum {

    // 待分账
    WAITING("waiting", "待分账"),
    PARTIAL_PROCESSING("partial_processing", "分账处理中"),
    PARTIAL_SUCCESS("partial_success", "部分分账完成"),
    FINISH_PROCESSING("finish_processing", "分账完结处理中"),
    FINISH_SUCCESS("finish_success", "分账完成"),
    PARTIAL_FAILED("partial_failed", "部分分账完成"),
    FINISH_FAILED("finish_failed", "分账失败"),
    CLOSED("closed", "分账关闭"),
    UNKNOWN("unknown", "分账状态未知");

    final String code;
    final String name;

}
