package cn.daxpay.open.channel.hmpay.convert.isv;

import cn.daxpay.open.channel.hmpay.entity.isv.HmpayIsvKeyConfig;
import cn.daxpay.open.channel.hmpay.param.isv.HmpayIsvKeyConfigParam;
import cn.daxpay.open.channel.hmpay.result.isv.HmpayIsvKeyConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 河马付服务商密钥配置转换
@Mapper
public interface HmpayIsvKeyConfigConvert {

    HmpayIsvKeyConfigConvert CONVERT = Mappers.getMapper(HmpayIsvKeyConfigConvert.class);

    /// 转换为返回对象
    HmpayIsvKeyConfigResult toResult(HmpayIsvKeyConfig entity);

    /// 转换为实体
    HmpayIsvKeyConfig toEntity(HmpayIsvKeyConfigParam param);

    /// 更新源数据到实体(空值不覆盖, 密钥为空时保留原值)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(HmpayIsvKeyConfigParam param, @MappingTarget HmpayIsvKeyConfig entity);
}
