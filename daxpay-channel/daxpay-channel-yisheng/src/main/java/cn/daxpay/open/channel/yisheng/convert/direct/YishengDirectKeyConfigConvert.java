package cn.daxpay.open.channel.yisheng.convert.direct;

import cn.daxpay.open.channel.yisheng.entity.direct.YishengDirectKeyConfig;
import cn.daxpay.open.channel.yisheng.param.direct.YishengDirectKeyConfigParam;
import cn.daxpay.open.channel.yisheng.result.direct.YishengDirectKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 易生直连密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface YishengDirectKeyConfigConvert {
    YishengDirectKeyConfigConvert CONVERT = Mappers.getMapper(YishengDirectKeyConfigConvert.class);

    YishengDirectKeyConfigResult toResult(YishengDirectKeyConfig entity);

    void copy(YishengDirectKeyConfigParam param, @MappingTarget YishengDirectKeyConfig entity);
}
