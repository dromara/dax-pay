package cn.daxpay.open.channel.sheng.convert;

import cn.daxpay.open.channel.sheng.entity.ShengKeyConfig;
import cn.daxpay.open.channel.sheng.param.ShengKeyConfigParam;
import cn.daxpay.open.channel.sheng.result.ShengKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 盛付通通道密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ShengKeyConfigConvert {
    ShengKeyConfigConvert CONVERT = Mappers.getMapper(ShengKeyConfigConvert.class);

    ShengKeyConfigResult toResult(ShengKeyConfig entity);

    /// 更新源数据到实体(空值不覆盖, 密钥为空时保留原值)
    void copy(ShengKeyConfigParam param, @MappingTarget ShengKeyConfig entity);
}
