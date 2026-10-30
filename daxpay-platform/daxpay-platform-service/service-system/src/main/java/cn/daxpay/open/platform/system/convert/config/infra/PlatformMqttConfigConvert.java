package cn.daxpay.open.platform.system.convert.config.infra;

import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformMqttConfig;
import cn.daxpay.open.platform.system.param.config.infra.PlatformMqttConfigParam;
import cn.daxpay.open.platform.system.result.config.infra.PlatformMqttConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 平台 MQTT 接入配置转换
@Mapper
public interface PlatformMqttConfigConvert {
    PlatformMqttConfigConvert CONVERT = Mappers.getMapper(PlatformMqttConfigConvert.class);

    PlatformMqttConfigResult toMqttResult(PlatformMqttConfig data);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(PlatformMqttConfigParam param, @MappingTarget PlatformMqttConfig data);
}
