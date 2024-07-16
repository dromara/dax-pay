package cn.daxpay.multi.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务消息通知类型
 * 业务系统使用什么样的方式接收异步通知消息
 * @author xxm
 * @since 2024/6/5
 */
@Getter
@AllArgsConstructor
public enum TradeNotifyTypeEnum {
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
