package cn.daxpay.open.channel.shouqianba.convert.direct;

import cn.daxpay.open.channel.shouqianba.entity.direct.ShouqianbaDirectKeyConfig;
import cn.daxpay.open.channel.shouqianba.param.direct.ShouqianbaDirectKeyConfigParam;
import cn.daxpay.open.channel.shouqianba.result.direct.ShouqianbaDirectKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 收钱吧直连密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ShouqianbaDirectKeyConfigConvert {
    ShouqianbaDirectKeyConfigConvert CONVERT = Mappers.getMapper(ShouqianbaDirectKeyConfigConvert.class);

    ShouqianbaDirectKeyConfigResult toResult(ShouqianbaDirectKeyConfig entity);

    void copy(ShouqianbaDirectKeyConfigParam param, @MappingTarget ShouqianbaDirectKeyConfig entity);
}
