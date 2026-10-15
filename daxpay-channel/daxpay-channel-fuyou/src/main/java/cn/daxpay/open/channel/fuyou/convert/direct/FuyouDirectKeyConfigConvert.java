package cn.daxpay.open.channel.fuyou.convert.direct;

import cn.daxpay.open.channel.fuyou.entity.direct.FuyouDirectKeyConfig;
import cn.daxpay.open.channel.fuyou.param.direct.FuyouDirectKeyConfigParam;
import cn.daxpay.open.channel.fuyou.result.direct.FuyouDirectKeyConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 富友直连商户密钥配置转换
@Mapper
public interface FuyouDirectKeyConfigConvert {

    FuyouDirectKeyConfigConvert CONVERT = Mappers.getMapper(FuyouDirectKeyConfigConvert.class);

    /// 转换为返回对象
    FuyouDirectKeyConfigResult toResult(FuyouDirectKeyConfig entity);

    /// 转换为实体
    FuyouDirectKeyConfig toEntity(FuyouDirectKeyConfigParam param);

    /// 更新源数据到实体(空值不覆盖, 密钥为空时保留原值)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(FuyouDirectKeyConfigParam param, @MappingTarget FuyouDirectKeyConfig entity);
}
