package cn.daxpay.open.channel.jeepay.convert;

import cn.daxpay.open.channel.jeepay.entity.JeepayKeyConfig;
import cn.daxpay.open.channel.jeepay.param.JeepayKeyConfigParam;
import cn.daxpay.open.channel.jeepay.result.JeepayKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # Jeepay通道密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface JeepayKeyConfigConvert {
    JeepayKeyConfigConvert CONVERT = Mappers.getMapper(JeepayKeyConfigConvert.class);

    JeepayKeyConfigResult toResult(JeepayKeyConfig entity);

    /// 更新源数据到实体(空值不覆盖, 密钥为空时保留原值)
    void copy(JeepayKeyConfigParam param, @MappingTarget JeepayKeyConfig entity);
}
