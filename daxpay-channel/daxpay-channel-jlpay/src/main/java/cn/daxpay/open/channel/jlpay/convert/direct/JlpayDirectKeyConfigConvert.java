package cn.daxpay.open.channel.jlpay.convert.direct;

import cn.daxpay.open.channel.jlpay.entity.direct.JlpayDirectKeyConfig;
import cn.daxpay.open.channel.jlpay.param.direct.JlpayDirectKeyConfigParam;
import cn.daxpay.open.channel.jlpay.result.direct.JlpayDirectKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 嘉联直连密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface JlpayDirectKeyConfigConvert {
    JlpayDirectKeyConfigConvert CONVERT = Mappers.getMapper(JlpayDirectKeyConfigConvert.class);

    JlpayDirectKeyConfigResult toResult(JlpayDirectKeyConfig entity);

    void copy(JlpayDirectKeyConfigParam param, @MappingTarget JlpayDirectKeyConfig entity);
}
