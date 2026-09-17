package cn.daxpay.open.payment.trade.order.convert.export;

import cn.daxpay.open.payment.common.convert.export.CommonExportConvertHelper;
import cn.daxpay.open.payment.trade.alloc.enums.TradeAllocStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import lombok.experimental.UtilityClass;

/// # 资金交易导出转换辅助(模块特有方法)
///
/// 仅保留 PayTrade 特有的枚举翻译(资金状态、交易形态、分账状态)。
/// 通用方法(金额换算、通道/渠道翻译、日期格式化)已移至 [CommonExportConvertHelper]。
public @UtilityClass class PayTradeExportConvertHelper {

    public String toStatusName(String code) {
        if (code == null) return "";
        return I18nUtil.getEnumName(PayFundStatusEnum.findByCode(code));
    }

    public String toTradeTypeName(String code) {
        if (code == null) return "";
        return I18nUtil.getEnumName(PayTradeTypeEnum.findByCode(code));
    }

    public String toAllocStatusName(String code) {
        if (code == null) return "";
        return I18nUtil.getEnumName(TradeAllocStatusEnum.findByCode(code));
    }
}
