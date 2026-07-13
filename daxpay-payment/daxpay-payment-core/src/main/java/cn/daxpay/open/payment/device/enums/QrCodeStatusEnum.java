package cn.daxpay.open.payment.device.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 码牌状态
///
/// 字典: qrcode_status
@Getter
@RequiredArgsConstructor
public enum QrCodeStatusEnum implements I18nSupport {

    /// 启用
    ENABLED("enabled"),
    /// 停用
    DISABLED("disabled");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.qrcode_status";
    }

    /// 根据编码查找
    public static QrCodeStatusEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 通用: 未找到对应的码牌状态: {0}
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.statusNotFound", code));
    }
}
