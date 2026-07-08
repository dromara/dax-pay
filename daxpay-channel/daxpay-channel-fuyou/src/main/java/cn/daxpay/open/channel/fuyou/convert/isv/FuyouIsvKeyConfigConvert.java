package cn.daxpay.open.channel.fuyou.convert.isv;

import cn.daxpay.open.channel.fuyou.entity.isv.FuyouIsvKeyConfig;
import cn.daxpay.open.channel.fuyou.param.isv.FuyouIsvKeyConfigParam;
import cn.daxpay.open.channel.fuyou.result.isv.FuyouIsvKeyConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 富友服务商密钥配置转换
@Mapper
public interface FuyouIsvKeyConfigConvert {

    FuyouIsvKeyConfigConvert CONVERT = Mappers.getMapper(FuyouIsvKeyConfigConvert.class);

    /// 转换为返回对象
    FuyouIsvKeyConfigResult toResult(FuyouIsvKeyConfig entity);

    /// 转换为实体
    FuyouIsvKeyConfig toEntity(FuyouIsvKeyConfigParam param);

    /// 更新源数据到实体(空值不覆盖, 密钥为空时保留原值)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(FuyouIsvKeyConfigParam param, @MappingTarget FuyouIsvKeyConfig entity);
}
