package org.dromara.daxpay.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 终端报送类型
 * 字典: terminal_type
 * @author xxm
 * @since 2025/3/12
 */
@Getter
@AllArgsConstructor
public enum TerminalTypeEnum {

    COMM("comm", "标准上报"),
    WECHAT("wechat", "微信渠道报送"),
    ALIPAY("alipay", "支付宝渠道报送"),
    UNION("union", "云闪付渠道报送"),
    ;

    private final String code;
    private final String name;
}
