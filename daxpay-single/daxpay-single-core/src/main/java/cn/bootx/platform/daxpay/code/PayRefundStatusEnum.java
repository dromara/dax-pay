package cn.bootx.platform.daxpay.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 退款状态枚举
 * @author xxm
 * @since 2023/12/18
 */
@Getter
@AllArgsConstructor
public enum PayRefundStatusEnum {

    /**
     * 接口调用成功不代表成功
     */
    PROGRESS("progress","退款中"),
    SUCCESS("success","成功"),
    FAIL("fail","失败");

    /** 编码 */
    private final String code;

    /** 名称 */
    private final String name;
}
