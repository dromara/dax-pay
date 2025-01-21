package org.dromara.daxpay.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息发送类型
 * 字典: notice_send_type
 * @author xxm
 * @since 2024/2/25
 */
@Getter
@AllArgsConstructor
public enum NoticeSendTypeEnum {

    /** 自动发送 */
    AUTO("auto", "自动发送"),
    /** 手动发送 */
    MANUAL("manual", "手动发送");

    private final String type;
    private final String name;
}
