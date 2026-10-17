package cn.daxpay.open.channel.epaylinks.convert.direct;

import cn.daxpay.open.channel.epaylinks.entity.direct.EpaylinksDirectKeyConfig;
import cn.daxpay.open.channel.epaylinks.param.direct.EpaylinksDirectKeyConfigParam;
import cn.daxpay.open.channel.epaylinks.result.direct.EpaylinksDirectKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 易票联直连密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EpaylinksDirectKeyConfigConvert {
    EpaylinksDirectKeyConfigConvert CONVERT = Mappers.getMapper(EpaylinksDirectKeyConfigConvert.class);

    EpaylinksDirectKeyConfigResult toResult(EpaylinksDirectKeyConfig entity);

    void copy(EpaylinksDirectKeyConfigParam param, @MappingTarget EpaylinksDirectKeyConfig entity);
}
