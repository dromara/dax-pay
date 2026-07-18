package cn.daxpay.open.platform.core.enums.pay.channel;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 通道终端设备报备状态
///
/// 字典: channel_terminal_status
/// 开源一期仅系统台账: 状态由运营人工维护, 不调用通道报备接口。
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

    /// 根据编码查找
    public static ChannelTerminalStatusEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 终端: 未找到对应的登记状态
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.statusNotFound", code));
    }
}

