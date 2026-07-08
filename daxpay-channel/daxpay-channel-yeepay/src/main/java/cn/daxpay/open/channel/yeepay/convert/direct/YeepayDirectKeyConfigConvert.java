package cn.daxpay.open.channel.yeepay.convert.direct;

import cn.daxpay.open.channel.yeepay.entity.direct.YeepayDirectKeyConfig;
import cn.daxpay.open.channel.yeepay.param.direct.YeepayDirectKeyConfigParam;
import cn.daxpay.open.channel.yeepay.result.direct.YeepayDirectKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 易宝直连密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface YeepayDirectKeyConfigConvert {
    YeepayDirectKeyConfigConvert CONVERT = Mappers.getMapper(YeepayDirectKeyConfigConvert.class);

    YeepayDirectKeyConfigResult toResult(YeepayDirectKeyConfig entity);

    void copy(YeepayDirectKeyConfigParam param, @MappingTarget YeepayDirectKeyConfig entity);
}
