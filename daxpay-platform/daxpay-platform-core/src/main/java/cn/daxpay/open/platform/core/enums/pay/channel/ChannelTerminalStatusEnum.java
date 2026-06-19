package cn.daxpay.open.platform.core.enums.pay.channel;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 通道终端设备报备状态
///
/// 字典: channel_terminal_status
@Getter
@RequiredArgsConstructor
public enum ChannelTerminalStatusEnum implements I18nSupport {

    /// 初始化
    INIT("init"),
    /// 未报送
    WAIT("wait"),
    /// 已报送
    SUBMIT("submit"),
    /// 已注销
    LOGGED("logged"),
    /// 错误
    ERROR("error");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.channel_terminal_status";
    }

}
