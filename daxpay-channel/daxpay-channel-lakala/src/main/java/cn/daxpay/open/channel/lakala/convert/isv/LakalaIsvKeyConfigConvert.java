package cn.daxpay.open.channel.lakala.convert.isv;

import cn.daxpay.open.channel.lakala.entity.isv.LakalaIsvKeyConfig;
import cn.daxpay.open.channel.lakala.param.isv.LakalaIsvKeyConfigParam;
import cn.daxpay.open.channel.lakala.result.isv.LakalaIsvKeyConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 拉卡拉服务商密钥配置转换
///
@Mapper
public interface LakalaIsvKeyConfigConvert {

    LakalaIsvKeyConfigConvert CONVERT = Mappers.getMapper(LakalaIsvKeyConfigConvert.class);

    /// 转换为返回对象
    LakalaIsvKeyConfigResult toResult(LakalaIsvKeyConfig entity);

    /// 转换为实体
    LakalaIsvKeyConfig toEntity(LakalaIsvKeyConfigParam param);

    /// 更新源数据到实体(空值不覆盖, 密钥/证书为空时保留原值)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(LakalaIsvKeyConfigParam param, @MappingTarget LakalaIsvKeyConfig entity);
}
