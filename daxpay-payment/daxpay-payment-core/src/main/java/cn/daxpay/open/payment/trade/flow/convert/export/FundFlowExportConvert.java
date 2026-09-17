package cn.daxpay.open.payment.trade.flow.convert.export;

import cn.daxpay.open.payment.common.convert.export.CommonExportConvertHelper;
import cn.daxpay.open.payment.trade.flow.result.FundFlowResult;
import cn.daxpay.open.payment.trade.flow.result.export.FundFlowExportResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/// # 资金流水 → 导出结果 转换
@Mapper(imports = CommonExportConvertHelper.class)
public interface FundFlowExportConvert {

    FundFlowExportConvert CONVERT = Mappers.getMapper(FundFlowExportConvert.class);

    @Mapping(target = "amount", expression = "java(CommonExportConvertHelper.toAmountStr(source.getAmount(), source.getCurrency()))")
    @Mapping(target = "flowType", expression = "java(FundFlowExportConvertHelper.toFlowTypeName(source.getFlowType()))")
    @Mapping(target = "channel", expression = "java(CommonExportConvertHelper.toChannelName(source.getChannel()))")
    @Mapping(target = "provider", expression = "java(CommonExportConvertHelper.toProviderName(source.getProvider()))")
    @Mapping(target = "finishTime", expression = "java(CommonExportConvertHelper.formatDateTime(source.getFinishTime()))")
    @Mapping(target = "createTime", expression = "java(CommonExportConvertHelper.formatDateTime(source.getCreateTime()))")
    FundFlowExportResult toExportResult(FundFlowResult source);

    List<FundFlowExportResult> toExportResultList(List<FundFlowResult> source);
}
