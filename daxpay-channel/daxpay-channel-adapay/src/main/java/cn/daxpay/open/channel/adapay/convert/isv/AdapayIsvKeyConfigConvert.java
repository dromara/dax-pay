package cn.daxpay.open.channel.adapay.convert.isv;

import cn.daxpay.open.channel.adapay.entity.isv.AdapayIsvKeyConfig;
import cn.daxpay.open.channel.adapay.param.isv.AdapayIsvKeyConfigParam;
import cn.daxpay.open.channel.adapay.result.isv.AdapayIsvKeyConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # Adapay 服务商密钥配置转换
@Mapper
public interface AdapayIsvKeyConfigConvert {

    AdapayIsvKeyConfigConvert CONVERT = Mappers.getMapper(AdapayIsvKeyConfigConvert.class);

    /// 转换为返回对象
    AdapayIsvKeyConfigResult toResult(AdapayIsvKeyConfig entity);

    /// 转换为实体
    AdapayIsvKeyConfig toEntity(AdapayIsvKeyConfigParam param);

    /// 更新源数据到实体(空值不覆盖, 密钥为空时保留原值)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(AdapayIsvKeyConfigParam param, @MappingTarget AdapayIsvKeyConfig entity);
}
