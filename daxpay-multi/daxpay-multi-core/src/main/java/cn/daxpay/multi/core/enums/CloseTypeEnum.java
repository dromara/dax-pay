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

    CLOSE("close","关闭"),
    CANCEL("cancel","撤销"),
    ;

    private final String code;
    private final String name;
}
