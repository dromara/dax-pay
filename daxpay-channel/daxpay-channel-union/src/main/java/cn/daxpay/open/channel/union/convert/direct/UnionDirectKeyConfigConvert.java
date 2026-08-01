package cn.daxpay.open.channel.union.convert.direct;

import cn.daxpay.open.channel.union.entity.direct.UnionDirectKeyConfig;
import cn.daxpay.open.channel.union.param.direct.UnionDirectKeyConfigParam;
import cn.daxpay.open.channel.union.result.direct.UnionDirectKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 云闪付直连密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UnionDirectKeyConfigConvert {
    UnionDirectKeyConfigConvert CONVERT = Mappers.getMapper(UnionDirectKeyConfigConvert.class);

    UnionDirectKeyConfigResult toResult(UnionDirectKeyConfig entity);

    void copy(UnionDirectKeyConfigParam param, @MappingTarget UnionDirectKeyConfig entity);
}
