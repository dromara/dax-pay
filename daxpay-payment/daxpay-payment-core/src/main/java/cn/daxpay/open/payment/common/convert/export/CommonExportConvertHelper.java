package cn.daxpay.open.payment.common.convert.export;

import cn.daxpay.open.payment.trade.util.CurrencyAmountUtil;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.common.request.context.RequestContextHolder;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.CurrencyEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.util.DateTimeUtil;
import lombok.experimental.UtilityClass;

import java.time.OffsetDateTime;

/// # 导出转换通用辅助方法
///
/// 各模块 ExportConvertHelper 共享的基础转换: 金额分→元、通道/渠道枚举翻译、日期格式化。
/// 模块特有的枚举翻译(如订单状态)仍放在各自的 ExportConvertHelper 中。
public @UtilityClass class CommonExportConvertHelper {

    public String toAmountStr(Long minorAmount, String currencyCode) {
        if (minorAmount == null || currencyCode == null) return "";
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

    /// 导出时间列格式化
    ///
    /// 导出文件与列表页展示必须一致, 故按**当前请求用户时区**(请求头 `x-timezone`)格式化;
    /// 缺头或非法时兜底业务时区(东八区)。库内时间为 UTC, 直接用无时区 formatter 会输出 UTC 墙上时间。
    public String formatDateTime(OffsetDateTime dateTime) {
        return DateTimeUtil.formatByZone(dateTime, RequestContextHolder.getTimeZone());
    }
}
