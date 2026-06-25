package cn.daxpay.open.payment.device.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 设备类型
///
/// 设备大类字典, 区分音箱/打印机/码牌等。
/// 字典: device_type
@Getter
@RequiredArgsConstructor
public enum DeviceTypeEnum implements I18nSupport {

    /// 云音箱
    SPEAKER("speaker"),
    /// 云打印
    PRINTER("printer"),
    /// 码牌(预留)
    QRCODE("qrcode");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.device_type";
    }
}
