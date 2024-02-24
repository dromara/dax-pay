package cn.bootx.platform.daxpay.service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息通知发送类型
 * @author xxm
 * @since 2024/2/25
 */
@Getter
@AllArgsConstructor
public enum ClientNoticeSendTypeEnum {
    /** 自动发送 */
    AUTO("auto", "自动发送"),
    /** 手动发送 */
    MANUAL("manual", "手动发送");

    private final String type;
    private final String name;

}
