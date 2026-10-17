package cn.daxpay.open.channel.llianpay.convert.direct;

import cn.daxpay.open.channel.llianpay.entity.direct.LlianpayDirectKeyConfig;
import cn.daxpay.open.channel.llianpay.param.direct.LlianpayDirectKeyConfigParam;
import cn.daxpay.open.channel.llianpay.result.direct.LlianpayDirectKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 连连支付直连密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LlianpayDirectKeyConfigConvert {
    LlianpayDirectKeyConfigConvert CONVERT = Mappers.getMapper(LlianpayDirectKeyConfigConvert.class);

    LlianpayDirectKeyConfigResult toResult(LlianpayDirectKeyConfig entity);

    void copy(LlianpayDirectKeyConfigParam param, @MappingTarget LlianpayDirectKeyConfig entity);
}
