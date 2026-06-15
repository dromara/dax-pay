package org.dromara.daxpay.channel.douyin.convert.direct;

import org.dromara.daxpay.channel.douyin.entity.direct.DouyinDirectApp;
import org.dromara.daxpay.channel.douyin.param.direct.DouyinDirectAppParam;
import org.dromara.daxpay.channel.douyin.result.direct.DouyinDirectAppResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 抖音直连商户应用转换
///
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DouyinDirectAppConvert {
    DouyinDirectAppConvert CONVERT = Mappers.getMapper(DouyinDirectAppConvert.class);

    DouyinDirectAppResult toResult(DouyinDirectApp entity);

    DouyinDirectApp toEntity(DouyinDirectAppParam param);

    void copy(DouyinDirectAppParam param, @MappingTarget DouyinDirectApp entity);
}
