package cn.daxpay.open.channel.yizhifubj.convert.direct;

import cn.daxpay.open.channel.yizhifubj.entity.direct.YizhifubjDirectKeyConfig;
import cn.daxpay.open.channel.yizhifubj.param.direct.YizhifubjDirectKeyConfigParam;
import cn.daxpay.open.channel.yizhifubj.result.direct.YizhifubjDirectKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 一直付直连密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface YizhifubjDirectKeyConfigConvert {
    YizhifubjDirectKeyConfigConvert CONVERT = Mappers.getMapper(YizhifubjDirectKeyConfigConvert.class);

    YizhifubjDirectKeyConfigResult toResult(YizhifubjDirectKeyConfig entity);

    void copy(YizhifubjDirectKeyConfigParam param, @MappingTarget YizhifubjDirectKeyConfig entity);
}
