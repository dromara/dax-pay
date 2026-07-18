package cn.daxpay.open.payment.device.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 终端报送类型
///
/// 字典: terminal_type
/// 部分通道统一登记用 common, 部分需按微信/支付宝/银联分类型登记。
@Getter
@RequiredArgsConstructor
public enum TerminalTypeEnum implements I18nSupport {

    /// 标准/统一上报
    COMMON("common"),
    /// 微信渠道报送
    WECHAT("wechat"),
    /// 支付宝渠道报送
    ALIPAY("alipay"),
    /// 银联/云闪付渠道报送
    UNION("union");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.terminal_type";
    }

    /// 根据编码查找
    public static TerminalTypeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 终端: 未找到对应的报送类型
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.typeNotFound", code));
    }
}
