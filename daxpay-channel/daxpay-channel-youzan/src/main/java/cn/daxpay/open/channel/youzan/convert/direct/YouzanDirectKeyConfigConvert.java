package cn.daxpay.open.channel.youzan.convert.direct;

import cn.daxpay.open.channel.youzan.entity.direct.YouzanDirectKeyConfig;
import cn.daxpay.open.channel.youzan.param.direct.YouzanDirectKeyConfigParam;
import cn.daxpay.open.channel.youzan.result.direct.YouzanDirectKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 有赞直连密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface YouzanDirectKeyConfigConvert {
    YouzanDirectKeyConfigConvert CONVERT = Mappers.getMapper(YouzanDirectKeyConfigConvert.class);

    YouzanDirectKeyConfigResult toResult(YouzanDirectKeyConfig entity);

    void copy(YouzanDirectKeyConfigParam param, @MappingTarget YouzanDirectKeyConfig entity);
}
