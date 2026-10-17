package cn.daxpay.open.channel.cpcn.convert.direct;

import cn.daxpay.open.channel.cpcn.entity.direct.CpcnDirectKeyConfig;
import cn.daxpay.open.channel.cpcn.param.direct.CpcnDirectKeyConfigParam;
import cn.daxpay.open.channel.cpcn.result.direct.CpcnDirectKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 中金直连密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CpcnDirectKeyConfigConvert {
    CpcnDirectKeyConfigConvert CONVERT = Mappers.getMapper(CpcnDirectKeyConfigConvert.class);

    CpcnDirectKeyConfigResult toResult(CpcnDirectKeyConfig entity);

    void copy(CpcnDirectKeyConfigParam param, @MappingTarget CpcnDirectKeyConfig entity);
}
