package cn.daxpay.open.channel.fubei.convert.direct;

import cn.daxpay.open.channel.fubei.entity.direct.FubeiDirectKeyConfig;
import cn.daxpay.open.channel.fubei.param.direct.FubeiDirectKeyConfigParam;
import cn.daxpay.open.channel.fubei.result.direct.FubeiDirectKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 付呗直连密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FubeiDirectKeyConfigConvert {
    FubeiDirectKeyConfigConvert CONVERT = Mappers.getMapper(FubeiDirectKeyConfigConvert.class);

    FubeiDirectKeyConfigResult toResult(FubeiDirectKeyConfig entity);

    void copy(FubeiDirectKeyConfigParam param, @MappingTarget FubeiDirectKeyConfig entity);
}
