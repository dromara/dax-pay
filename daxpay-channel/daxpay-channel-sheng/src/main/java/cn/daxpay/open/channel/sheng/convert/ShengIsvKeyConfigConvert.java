package cn.daxpay.open.channel.sheng.convert;

import cn.daxpay.open.channel.sheng.entity.ShengIsvKeyConfig;
import cn.daxpay.open.channel.sheng.param.ShengIsvKeyConfigParam;
import cn.daxpay.open.channel.sheng.result.ShengIsvKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 盛付通服务商密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ShengIsvKeyConfigConvert {
    ShengIsvKeyConfigConvert CONVERT = Mappers.getMapper(ShengIsvKeyConfigConvert.class);

    ShengIsvKeyConfigResult toResult(ShengIsvKeyConfig entity);

    /// 更新源数据到实体(空值不覆盖, 密钥为空时保留原值)
    void copy(ShengIsvKeyConfigParam param, @MappingTarget ShengIsvKeyConfig entity);
}
