package org.dromara.daxpay.channel.douyin.convert.direct;

import org.dromara.daxpay.channel.douyin.entity.direct.DouyinDirectKeyConfig;
import org.dromara.daxpay.channel.douyin.param.direct.DouyinDirectKeyConfigParam;
import org.dromara.daxpay.channel.douyin.result.direct.DouyinDirectKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 抖音直连密钥配置转换
///
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DouyinDirectKeyConfigConvert {
    DouyinDirectKeyConfigConvert CONVERT = Mappers.getMapper(DouyinDirectKeyConfigConvert.class);

    DouyinDirectKeyConfigResult toResult(DouyinDirectKeyConfig entity);

    void copy(DouyinDirectKeyConfigParam param, @MappingTarget DouyinDirectKeyConfig entity);
}
