package cn.daxpay.open.payment.trade.order.convert.export;

import cn.daxpay.open.payment.common.convert.export.CommonExportConvertHelper;
import cn.daxpay.open.payment.trade.order.result.RefundOrderResult;
import cn.daxpay.open.payment.trade.order.result.export.RefundOrderExportResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/// # 退款订单 → 导出结果 转换
///
/// 负责金额分→元换算、枚举国际化翻译、时间格式化。
/// 通用方法委托 [CommonExportConvertHelper], 模块特有方法委托 [RefundOrderExportConvertHelper]。
@Mapper(imports = CommonExportConvertHelper.class)
public interface RefundOrderExportConvert {

    RefundOrderExportConvert CONVERT = Mappers.getMapper(RefundOrderExportConvert.class);

    @Mapping(target = "amount", expression = "java(CommonExportConvertHelper.toAmountStr(source.getAmount(), source.getCurrency()))")
    @Mapping(target = "orderAmount", expression = "java(CommonExportConvertHelper.toAmountStr(source.getOrderAmount(), source.getCurrency()))")
    @Mapping(target = "status", expression = "java(RefundOrderExportConvertHelper.toStatusName(source.getStatus()))")
    @Mapping(target = "tradeType", expression = "java(RefundOrderExportConvertHelper.toTradeTypeName(source.getTradeType()))")
    @Mapping(target = "channel", expression = "java(CommonExportConvertHelper.toChannelName(source.getChannel()))")
    @Mapping(target = "product", expression = "java(CommonExportConvertHelper.toProductName(source.getProduct()))")
    @Mapping(target = "finishTime", expression = "java(CommonExportConvertHelper.formatDateTime(source.getFinishTime()))")
    @Mapping(target = "createTime", expression = "java(CommonExportConvertHelper.formatDateTime(source.getCreateTime()))")
    RefundOrderExportResult toExportResult(RefundOrderResult source);

    List<RefundOrderExportResult> toExportResultList(List<RefundOrderResult> source);
}
