package cn.daxpay.single.service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单关闭类型
 * @author xxm
 * @since 2024/6/5
 */
@Getter
@AllArgsConstructor
public enum PayCloseTypeEnum {
    /**
     * 订单关闭
     */
    CLOSE("close", "订单关闭"),
    /**
     * 订单撤销
     */
    CANCEL("cancel", "订单撤销"),
    ;
    final String code;
    final String name;
}
