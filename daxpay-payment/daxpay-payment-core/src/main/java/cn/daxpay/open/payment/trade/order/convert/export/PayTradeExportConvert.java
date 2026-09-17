package cn.daxpay.open.payment.trade.order.convert.export;

import cn.daxpay.open.payment.common.convert.export.CommonExportConvertHelper;
import cn.daxpay.open.payment.trade.order.result.PayTradeResult;
import cn.daxpay.open.payment.trade.order.result.export.PayTradeExportResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/// # 资金交易凭证 → 导出结果 转换
///
/// 负责金额分→元换算、枚举国际化翻译、时间格式化。
/// 通用方法委托 [CommonExportConvertHelper], 模块特有方法委托 [PayTradeExportConvertHelper]。
@Mapper(imports = CommonExportConvertHelper.class)
public interface PayTradeExportConvert {

    PayTradeExportConvert CONVERT = Mappers.getMapper(PayTradeExportConvert.class);

    @Mapping(target = "amount", expression = "java(CommonExportConvertHelper.toAmountStr(source.getAmount(), source.getCurrency()))")
    @Mapping(target = "postedAmount", expression = "java(CommonExportConvertHelper.toAmountStr(source.getPostedAmount(), source.getCurrency()))")
    @Mapping(target = "refundableBalance", expression = "java(CommonExportConvertHelper.toAmountStr(source.getRefundableBalance(), source.getCurrency()))")
    @Mapping(target = "status", expression = "java(PayTradeExportConvertHelper.toStatusName(source.getStatus()))")
    @Mapping(target = "tradeType", expression = "java(PayTradeExportConvertHelper.toTradeTypeName(source.getTradeType()))")
    @Mapping(target = "channel", expression = "java(CommonExportConvertHelper.toChannelName(source.getChannel()))")
    @Mapping(target = "provider", expression = "java(CommonExportConvertHelper.toProviderName(source.getProvider()))")
    @Mapping(target = "product", expression = "java(CommonExportConvertHelper.toProductName(source.getProduct()))")
    @Mapping(target = "method", expression = "java(CommonExportConvertHelper.toMethodName(source.getMethod()))")
    @Mapping(target = "source", expression = "java(CommonExportConvertHelper.toSourceName(source.getSource()))")
    @Mapping(target = "allocStatus", expression = "java(PayTradeExportConvertHelper.toAllocStatusName(source.getAllocStatus()))")
    @Mapping(target = "payTime", expression = "java(CommonExportConvertHelper.formatDateTime(source.getPayTime()))")
    @Mapping(target = "createTime", expression = "java(CommonExportConvertHelper.formatDateTime(source.getCreateTime()))")
    PayTradeExportResult toExportResult(PayTradeResult source);

    List<PayTradeExportResult> toExportResultList(List<PayTradeResult> source);
}
