package cn.daxpay.open.channel.longpay.convert.direct;

import cn.daxpay.open.channel.longpay.entity.direct.LongpayDirectKeyConfig;
import cn.daxpay.open.channel.longpay.param.direct.LongpayDirectKeyConfigParam;
import cn.daxpay.open.channel.longpay.result.direct.LongpayDirectKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 建行龙支付直连密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LongpayDirectKeyConfigConvert {
    LongpayDirectKeyConfigConvert CONVERT = Mappers.getMapper(LongpayDirectKeyConfigConvert.class);

    LongpayDirectKeyConfigResult toResult(LongpayDirectKeyConfig entity);

    void copy(LongpayDirectKeyConfigParam param, @MappingTarget LongpayDirectKeyConfig entity);
}
