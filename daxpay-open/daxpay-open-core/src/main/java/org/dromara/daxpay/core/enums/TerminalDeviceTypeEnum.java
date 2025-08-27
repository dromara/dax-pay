package org.dromara.daxpay.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 收款终端设备类型
 * 字典: terminal_device_type
 * @author xxm
 * @since 2025/3/7
 */
@Getter
@AllArgsConstructor
public enum TerminalDeviceTypeEnum {

    AUTO_COUNTER("01", "自动柜员机"),
    TRADITIONAL_POS("02", "传统POS"),
    MPOS("03", "mPOS"),
    SMART_POS("04", "智能POS"),
    FIXED_PHONE("05", "II型固定电话"),
    CLOUD_SHAN_PAY("06", "云闪付终端"),
    RESERVE_USE("07", "保留使用"),
    PHONE_POS("08", "手机POS"),
    FACE_PAY_TERMINAL("09", "刷脸付终端"),
    BAR_CODE_PAY_ACCEPT_TERMINAL("10", "条码支付受理终端"),
    ASSIST_ACCEPT_TERMINAL("11", "辅助受理终端"),
    INDUSTRY_TERMINAL("12", "行业终端"),
    MIS_TERMINAL("13", "MIS终端");

    private final String code;
    private final String name;
}
