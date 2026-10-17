package cn.daxpay.open.channel.ysep.convert;

import cn.daxpay.open.channel.ysep.entity.YsepKeyConfig;
import cn.daxpay.open.channel.ysep.param.YsepKeyConfigParam;
import cn.daxpay.open.channel.ysep.result.YsepKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 银盛通道密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface YsepKeyConfigConvert {
    YsepKeyConfigConvert CONVERT = Mappers.getMapper(YsepKeyConfigConvert.class);

    YsepKeyConfigResult toResult(YsepKeyConfig entity);

    /// 更新源数据到实体(空值不覆盖, 证书/密码为空时保留原值)
    void copy(YsepKeyConfigParam param, @MappingTarget YsepKeyConfig entity);
}
