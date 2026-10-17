package cn.daxpay.open.channel.sand.convert.direct;

import cn.daxpay.open.channel.sand.entity.direct.SandDirectKeyConfig;
import cn.daxpay.open.channel.sand.param.direct.SandDirectKeyConfigParam;
import cn.daxpay.open.channel.sand.result.direct.SandDirectKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 杉德直连密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SandDirectKeyConfigConvert {
    SandDirectKeyConfigConvert CONVERT = Mappers.getMapper(SandDirectKeyConfigConvert.class);

    SandDirectKeyConfigResult toResult(SandDirectKeyConfig entity);

    void copy(SandDirectKeyConfigParam param, @MappingTarget SandDirectKeyConfig entity);
}
