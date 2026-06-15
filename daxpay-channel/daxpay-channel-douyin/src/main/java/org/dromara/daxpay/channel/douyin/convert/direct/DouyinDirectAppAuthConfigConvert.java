package org.dromara.daxpay.channel.douyin.convert.direct;

import org.dromara.daxpay.channel.douyin.entity.direct.DouyinDirectAppAuthConfig;
import org.dromara.daxpay.channel.douyin.param.direct.DouyinDirectAppAuthConfigParam;
import org.dromara.daxpay.channel.douyin.result.direct.DouyinDirectAppAuthConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 抖音直连商户应用授权认证配置转换
///
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DouyinDirectAppAuthConfigConvert {
    DouyinDirectAppAuthConfigConvert CONVERT = Mappers.getMapper(DouyinDirectAppAuthConfigConvert.class);

    DouyinDirectAppAuthConfigResult toResult(DouyinDirectAppAuthConfig entity);

    void copy(DouyinDirectAppAuthConfigParam param, @MappingTarget DouyinDirectAppAuthConfig entity);
}
