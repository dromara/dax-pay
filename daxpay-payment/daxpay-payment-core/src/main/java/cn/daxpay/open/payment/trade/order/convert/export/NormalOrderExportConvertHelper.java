package cn.daxpay.open.payment.trade.order.convert.export;

import cn.daxpay.open.payment.trade.enums.NormalPayOrderStatusEnum;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import lombok.experimental.UtilityClass;

/// # 普通支付业务单导出转换辅助方法
///
/// 模块特有的枚举翻译: 业务状态。
/// 通用转换(金额/通道/渠道/时间)委托 [cn.daxpay.open.payment.common.convert.export.CommonExportConvertHelper]。
public @UtilityClass class NormalOrderExportConvertHelper {

    public String toStatusName(String code) {
        if (code == null) return "";
        return I18nUtil.getEnumName(NormalPayOrderStatusEnum.findByCode(code));
    }
}
