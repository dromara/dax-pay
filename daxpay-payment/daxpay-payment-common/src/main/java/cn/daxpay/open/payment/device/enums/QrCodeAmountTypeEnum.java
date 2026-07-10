package cn.daxpay.open.payment.device.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 码牌金额类型
///
/// 字典: qrcode_amount_type
@Getter
@RequiredArgsConstructor
public enum QrCodeAmountTypeEnum implements I18nSupport {

    /// 自定义金额(扫码后由用户输入)
    RANDOM("random"),
    /// 固定金额(创建时设定, 扫码后直接支付)
    FIXED("fixed");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.qrcode_amount_type";
    }

    /// 根据编码查找
    public static QrCodeAmountTypeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 通用: 未找到对应的码牌金额类型: {0}
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.amountTypeNotFound", code));
    }
}
