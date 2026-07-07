package cn.daxpay.open.channel.hkrt.convert.isv;

import cn.daxpay.open.channel.hkrt.entity.isv.HkrtIsvKeyConfig;
import cn.daxpay.open.channel.hkrt.param.isv.HkrtIsvKeyConfigParam;
import cn.daxpay.open.channel.hkrt.result.isv.HkrtIsvKeyConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 海科融通服务商密钥配置转换
///
@Mapper
public interface HkrtIsvKeyConfigConvert {

    HkrtIsvKeyConfigConvert CONVERT = Mappers.getMapper(HkrtIsvKeyConfigConvert.class);

    /// 转换为返回对象
    HkrtIsvKeyConfigResult toResult(HkrtIsvKeyConfig entity);

    /// 转换为实体
    HkrtIsvKeyConfig toEntity(HkrtIsvKeyConfigParam param);

    /// 更新源数据到实体(空值不覆盖, 密钥为空时保留原值)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(HkrtIsvKeyConfigParam param, @MappingTarget HkrtIsvKeyConfig entity);
}
