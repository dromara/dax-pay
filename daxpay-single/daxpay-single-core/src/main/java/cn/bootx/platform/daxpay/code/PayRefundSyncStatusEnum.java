package cn.bootx.platform.daxpay.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 退款同步状态枚举
 * @author xxm
 * @since 2024/1/29
 */
@Getter
@AllArgsConstructor
public enum PayRefundSyncStatusEnum {
    SUCCESS("success","成功"),
    FAIL("fail","失败"),
    REFUNDING("refunding","退款中");

    /** 编码 */
    private final String code;
    /** 名称 */
    private final String name;
}
