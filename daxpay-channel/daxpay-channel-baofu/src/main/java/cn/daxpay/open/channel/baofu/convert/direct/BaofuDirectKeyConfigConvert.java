package cn.daxpay.open.channel.baofu.convert.direct;

import cn.daxpay.open.channel.baofu.entity.direct.BaofuDirectKeyConfig;
import cn.daxpay.open.channel.baofu.param.direct.BaofuDirectKeyConfigParam;
import cn.daxpay.open.channel.baofu.result.direct.BaofuDirectKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 宝付直连密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BaofuDirectKeyConfigConvert {
    BaofuDirectKeyConfigConvert CONVERT = Mappers.getMapper(BaofuDirectKeyConfigConvert.class);

    BaofuDirectKeyConfigResult toResult(BaofuDirectKeyConfig entity);

    void copy(BaofuDirectKeyConfigParam param, @MappingTarget BaofuDirectKeyConfig entity);
}
