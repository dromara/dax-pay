package cn.daxpay.open.channel.umfpay.convert.direct;

import cn.daxpay.open.channel.umfpay.entity.direct.UmfpayDirectKeyConfig;
import cn.daxpay.open.channel.umfpay.param.direct.UmfpayDirectKeyConfigParam;
import cn.daxpay.open.channel.umfpay.result.direct.UmfpayDirectKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 联动优势直连密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UmfpayDirectKeyConfigConvert {
    UmfpayDirectKeyConfigConvert CONVERT = Mappers.getMapper(UmfpayDirectKeyConfigConvert.class);

    UmfpayDirectKeyConfigResult toResult(UmfpayDirectKeyConfig entity);

    void copy(UmfpayDirectKeyConfigParam param, @MappingTarget UmfpayDirectKeyConfig entity);
}
