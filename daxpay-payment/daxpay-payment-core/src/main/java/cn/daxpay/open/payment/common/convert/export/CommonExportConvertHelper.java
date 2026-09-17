package cn.daxpay.open.payment.common.convert.export;

import cn.daxpay.open.payment.trade.util.CurrencyAmountUtil;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.CurrencyEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import lombok.experimental.UtilityClass;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/// # 导出转换通用辅助方法
///
/// 各模块 ExportConvertHelper 共享的基础转换: 金额分→元、通道/渠道枚举翻译、日期格式化。
/// 模块特有的枚举翻译(如订单状态)仍放在各自的 ExportConvertHelper 中。
public @UtilityClass class CommonExportConvertHelper {

    private final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String toAmountStr(Long minorAmount, String currencyCode) {
        if (minorAmount == null) return "";
        CurrencyEnum currency = CurrencyEnum.findByCode(currencyCode);
        return CurrencyAmountUtil.minorToMajorStr(minorAmount, currency);
    }

    public String toChannelName(String code) {
        if (code == null) return "";
        return I18nUtil.getEnumName(ChannelEnum.findByCode(code));
    }

    public String toProviderName(String code) {
        if (code == null) return "";
        return I18nUtil.getEnumName(PayProviderEnum.findByCode(code));
    }

    public String formatDateTime(OffsetDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(DATE_TIME_FMT);
    }
}
