package cn.daxpay.open.channel.ums.convert.direct;

import cn.daxpay.open.channel.ums.entity.direct.UmsDirectKeyConfig;
import cn.daxpay.open.channel.ums.param.direct.UmsDirectKeyConfigParam;
import cn.daxpay.open.channel.ums.result.direct.UmsDirectKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 银联商务直连密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UmsDirectKeyConfigConvert {
    UmsDirectKeyConfigConvert CONVERT = Mappers.getMapper(UmsDirectKeyConfigConvert.class);

    UmsDirectKeyConfigResult toResult(UmsDirectKeyConfig entity);

    void copy(UmsDirectKeyConfigParam param, @MappingTarget UmsDirectKeyConfig entity);
}
