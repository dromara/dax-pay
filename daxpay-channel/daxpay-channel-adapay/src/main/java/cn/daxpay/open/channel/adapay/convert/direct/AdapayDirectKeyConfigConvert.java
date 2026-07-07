package cn.daxpay.open.channel.adapay.convert.direct;

import cn.daxpay.open.channel.adapay.entity.direct.AdapayDirectKeyConfig;
import cn.daxpay.open.channel.adapay.param.direct.AdapayDirectKeyConfigParam;
import cn.daxpay.open.channel.adapay.result.direct.AdapayDirectKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 汇付天下直连密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AdapayDirectKeyConfigConvert {

    AdapayDirectKeyConfigConvert CONVERT = Mappers.getMapper(AdapayDirectKeyConfigConvert.class);

    AdapayDirectKeyConfigResult toResult(AdapayDirectKeyConfig entity);

    void copy(AdapayDirectKeyConfigParam param, @MappingTarget AdapayDirectKeyConfig entity);
}
