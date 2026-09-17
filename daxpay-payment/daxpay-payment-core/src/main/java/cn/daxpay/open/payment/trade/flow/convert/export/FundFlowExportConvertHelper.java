package cn.daxpay.open.payment.trade.flow.convert.export;

import cn.daxpay.open.payment.trade.flow.enums.FundFlowTypeEnum;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import lombok.experimental.UtilityClass;

/// # 资金流水导出转换辅助(模块特有方法)
///
/// 通用方法(金额换算、通道/渠道翻译、日期格式化)已移至 [CommonExportConvertHelper]。
public @UtilityClass class FundFlowExportConvertHelper {

    public String toFlowTypeName(String code) {
        if (code == null) return "";
        return I18nUtil.getEnumName(FundFlowTypeEnum.findByCode(code));
    }
}
