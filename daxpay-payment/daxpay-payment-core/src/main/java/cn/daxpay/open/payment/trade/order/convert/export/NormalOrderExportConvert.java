package cn.daxpay.open.payment.trade.order.convert.export;

import cn.daxpay.open.payment.common.convert.export.CommonExportConvertHelper;
import cn.daxpay.open.payment.trade.order.result.NormalPayOrderResult;
import cn.daxpay.open.payment.trade.order.result.export.NormalOrderExportResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/// # 普通支付业务单 → 导出结果 转换
///
/// 负责金额分→元换算、枚举国际化翻译、时间格式化。
/// 通用方法委托 [CommonExportConvertHelper], 模块特有方法委托 [NormalOrderExportConvertHelper]。
@Mapper(imports = CommonExportConvertHelper.class)
public interface NormalOrderExportConvert {

    NormalOrderExportConvert CONVERT = Mappers.getMapper(NormalOrderExportConvert.class);

    @Mapping(target = "amount", expression = "java(CommonExportConvertHelper.toAmountStr(source.getAmount(), source.getCurrency()))")
    @Mapping(target = "status", expression = "java(NormalOrderExportConvertHelper.toStatusName(source.getStatus()))")
    @Mapping(target = "channel", expression = "java(CommonExportConvertHelper.toChannelName(source.getChannel()))")
    @Mapping(target = "payTime", expression = "java(CommonExportConvertHelper.formatDateTime(source.getPayTime()))")
    @Mapping(target = "createTime", expression = "java(CommonExportConvertHelper.formatDateTime(source.getCreateTime()))")
    NormalOrderExportResult toExportResult(NormalPayOrderResult source);

    List<NormalOrderExportResult> toExportResultList(List<NormalPayOrderResult> source);
}
