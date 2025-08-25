package org.dromara.daxpay.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通道终端设备报备状态
 * 字典: channel_terminal_status
 * @author xxm
 * @since 2025/3/9
 */
@Getter
@AllArgsConstructor
public enum ChannelTerminalStatusEnum {

    WAIT("wait", "未报送"),
    SUBMIT("submit", "已报送"),
    LOGGED("logged", "已注销"),
    ERROR("error", "错误");

    private final String code;
    private final String name;
}
