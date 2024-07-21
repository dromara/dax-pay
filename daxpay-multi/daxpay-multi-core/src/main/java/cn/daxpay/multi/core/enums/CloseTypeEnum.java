package cn.daxpay.multi.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付订单关闭类型
 * @author xxm
 * @since 2024/6/3
 */
@Getter
@AllArgsConstructor
public enum CloseTypeEnum {

    /** 关闭 */
    CLOSE("close", PayStatusEnum.CLOSE),
    /** 撤销 */
    CANCEL("cancel", PayStatusEnum.CANCEL),
    ;

    private final String code;
    private final PayStatusEnum payStatus;
}
