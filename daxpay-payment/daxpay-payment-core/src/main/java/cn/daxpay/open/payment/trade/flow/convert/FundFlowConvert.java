package cn.daxpay.open.payment.trade.flow.convert;

import cn.daxpay.open.payment.trade.flow.entity.FundFlow;
import cn.daxpay.open.payment.trade.flow.result.FundFlowResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 资金流水转换
///
@Mapper
public interface FundFlowConvert {

    FundFlowConvert CONVERT = Mappers.getMapper(FundFlowConvert.class);

    FundFlowResult toResult(FundFlow entity);
}
