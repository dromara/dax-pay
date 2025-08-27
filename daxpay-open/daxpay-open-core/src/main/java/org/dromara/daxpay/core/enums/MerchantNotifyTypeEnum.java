package org.dromara.daxpay.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商户消息通知类型, 业务系统使用什么样的方式接收通知消息
 * 字典: merchant_notify_type
 * @author xxm
 * @since 2024/6/5
 */
@Getter
@AllArgsConstructor
public enum MerchantNotifyTypeEnum {
    /** http */
    HTTP("http"),
    /** websocket */
    WEBSOCKET("websocket"),
    /** mq */
    MQ("mq"),
    /** 不启用 */
    NONE("none");

    private final String code;
}
