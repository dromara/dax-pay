package cn.daxpay.open.channel.douyin.convert.direct;

import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectApp;
import cn.daxpay.open.channel.douyin.param.direct.DouyinDirectAppParam;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectAppResult;
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
