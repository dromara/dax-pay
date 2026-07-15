package cn.daxpay.open.payment.device.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 码牌落地程序类型
///
/// 扫码链接 path 按类型分流: H5 为 `/h/{code}`, 小程序为 `/m/{code}`。
/// 字典: qrcode_program_type
@Getter
@RequiredArgsConstructor
public enum QrCodeProgramTypeEnum implements I18nSupport {

    /// H5 码牌(扫码进入支付网关 H5 页)
    H5("h5"),
    /// 小程序码牌(扫码进入小程序映射 path, 落地页后置)
    MINI_APP("mini_app");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.qrcode_program_type";
    }

    /// 根据编码查找
    public static QrCodeProgramTypeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 通用: 未找到对应的码牌程序类型: {0}
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.programTypeNotFound", code));
    }
}
