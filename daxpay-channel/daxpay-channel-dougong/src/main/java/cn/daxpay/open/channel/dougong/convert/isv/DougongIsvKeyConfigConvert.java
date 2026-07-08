package cn.daxpay.open.channel.dougong.convert.isv;

import cn.daxpay.open.channel.dougong.entity.isv.DougongIsvKeyConfig;
import cn.daxpay.open.channel.dougong.param.isv.DougongIsvKeyConfigParam;
import cn.daxpay.open.channel.dougong.result.isv.DougongIsvKeyConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 斗拱服务商密钥配置转换
@Mapper
public interface DougongIsvKeyConfigConvert {

    DougongIsvKeyConfigConvert CONVERT = Mappers.getMapper(DougongIsvKeyConfigConvert.class);

    /// 转换为返回对象
    DougongIsvKeyConfigResult toResult(DougongIsvKeyConfig entity);

    /// 转换为实体
    DougongIsvKeyConfig toEntity(DougongIsvKeyConfigParam param);

    /// 更新源数据到实体(空值不覆盖, 密钥为空时保留原值)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(DougongIsvKeyConfigParam param, @MappingTarget DougongIsvKeyConfig entity);
}
