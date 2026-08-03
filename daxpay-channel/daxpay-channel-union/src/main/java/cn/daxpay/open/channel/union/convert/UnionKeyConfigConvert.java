package cn.daxpay.open.channel.union.convert;

import cn.daxpay.open.channel.union.entity.UnionKeyConfig;
import cn.daxpay.open.channel.union.param.UnionKeyConfigParam;
import cn.daxpay.open.channel.union.result.UnionKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 云闪付密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UnionKeyConfigConvert {
    UnionKeyConfigConvert CONVERT = Mappers.getMapper(UnionKeyConfigConvert.class);

    UnionKeyConfigResult toResult(UnionKeyConfig entity);

    void copy(UnionKeyConfigParam param, @MappingTarget UnionKeyConfig entity);
}
