package cn.daxpay.open.payment.trade.order.convert.export;

import cn.daxpay.open.payment.common.convert.export.CommonExportConvertHelper;
import cn.daxpay.open.payment.trade.order.result.GatewayPayOrderResult;
import cn.daxpay.open.payment.trade.order.result.export.GatewayOrderExportResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/// # 网关支付业务单 → 导出结果 转换
///
/// 负责金额分→元换算、枚举国际化翻译、时间格式化。
/// 通用方法委托 [CommonExportConvertHelper], 模块特有方法委托 [GatewayOrderExportConvertHelper]。
@Mapper(imports = CommonExportConvertHelper.class)
public interface GatewayOrderExportConvert {

    GatewayOrderExportConvert CONVERT = Mappers.getMapper(GatewayOrderExportConvert.class);

    @Mapping(target = "amount", expression = "java(CommonExportConvertHelper.toAmountStr(source.getAmount(), source.getCurrency()))")
    @Mapping(target = "status", expression = "java(GatewayOrderExportConvertHelper.toStatusName(source.getStatus()))")
    @Mapping(target = "gatewayType", expression = "java(GatewayOrderExportConvertHelper.toGatewayTypeName(source.getGatewayType()))")
    @Mapping(target = "channel", expression = "java(CommonExportConvertHelper.toChannelName(source.getChannel()))")
    @Mapping(target = "provider", expression = "java(CommonExportConvertHelper.toProviderName(source.getProvider()))")
    @Mapping(target = "payTime", expression = "java(CommonExportConvertHelper.formatDateTime(source.getPayTime()))")
    @Mapping(target = "createTime", expression = "java(CommonExportConvertHelper.formatDateTime(source.getCreateTime()))")
    GatewayOrderExportResult toExportResult(GatewayPayOrderResult source);

    List<GatewayOrderExportResult> toExportResultList(List<GatewayPayOrderResult> source);
}
