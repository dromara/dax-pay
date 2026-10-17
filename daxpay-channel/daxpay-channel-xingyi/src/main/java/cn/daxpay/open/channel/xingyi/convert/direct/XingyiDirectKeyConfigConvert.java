package cn.daxpay.open.channel.xingyi.convert.direct;

import cn.daxpay.open.channel.xingyi.entity.direct.XingyiDirectKeyConfig;
import cn.daxpay.open.channel.xingyi.param.direct.XingyiDirectKeyConfigParam;
import cn.daxpay.open.channel.xingyi.result.direct.XingyiDirectKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 星驿付直连密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface XingyiDirectKeyConfigConvert {
    XingyiDirectKeyConfigConvert CONVERT = Mappers.getMapper(XingyiDirectKeyConfigConvert.class);

    XingyiDirectKeyConfigResult toResult(XingyiDirectKeyConfig entity);

    void copy(XingyiDirectKeyConfigParam param, @MappingTarget XingyiDirectKeyConfig entity);
}
