package cn.daxpay.open.payment.trade.order.convert.export;

import cn.daxpay.open.payment.trade.enums.GatewayOrderStatusEnum;
import cn.daxpay.open.payment.trade.enums.GatewayPayTypeEnum;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import lombok.experimental.UtilityClass;

/// # 网关支付业务单导出转换辅助方法
///
/// 模块特有的枚举翻译: 业务状态、网关类型。
/// 通用转换(金额/通道/渠道/时间)委托 [cn.daxpay.open.payment.common.convert.export.CommonExportConvertHelper]。
public @UtilityClass class GatewayOrderExportConvertHelper {

    public String toStatusName(String code) {
        if (code == null) return "";
        return I18nUtil.getEnumName(GatewayOrderStatusEnum.findByCode(code));
    }

    public String toGatewayTypeName(String code) {
        if (code == null) return "";
        return I18nUtil.getEnumName(GatewayPayTypeEnum.findByCode(code));
    }
}
