package cn.daxpay.open.plugin.easypay.convert;

import cn.daxpay.open.plugin.easypay.entity.EasyPayOrder;
import cn.daxpay.open.plugin.easypay.result.order.EasyPayOrderResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EasyPayOrderConvert {
    EasyPayOrderConvert CONVERT = Mappers.getMapper(EasyPayOrderConvert.class);

    EasyPayOrderResult toResult(EasyPayOrder entity);
}
