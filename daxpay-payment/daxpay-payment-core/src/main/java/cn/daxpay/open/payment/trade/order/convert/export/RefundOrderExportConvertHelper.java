package cn.daxpay.open.payment.trade.order.convert.export;

import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import lombok.experimental.UtilityClass;

/// # 退款订单导出转换辅助方法
///
/// 模块特有的枚举翻译: 退款状态、交易类型。
/// 通用转换(金额/通道/渠道/时间)委托 [cn.daxpay.open.payment.common.convert.export.CommonExportConvertHelper]。
public @UtilityClass class RefundOrderExportConvertHelper {

    public String toStatusName(String code) {
        if (code == null) return "";
        return I18nUtil.getEnumName(RefundOrderStatusEnum.findByCode(code));
    }

    public String toTradeTypeName(String code) {
        if (code == null) return "";
        return I18nUtil.getEnumName(PayTradeTypeEnum.findByCode(code));
    }
}
