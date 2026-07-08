package cn.daxpay.open.channel.leshua.convert.isv;

import cn.daxpay.open.channel.leshua.entity.isv.LeshuaIsvKeyConfig;
import cn.daxpay.open.channel.leshua.param.isv.LeshuaIsvKeyConfigParam;
import cn.daxpay.open.channel.leshua.result.isv.LeshuaIsvKeyConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 乐刷服务商密钥配置转换
///
@Mapper
public interface LeshuaIsvKeyConfigConvert {

    LeshuaIsvKeyConfigConvert CONVERT = Mappers.getMapper(LeshuaIsvKeyConfigConvert.class);

    /// 转换为返回对象
    LeshuaIsvKeyConfigResult toResult(LeshuaIsvKeyConfig entity);

    /// 转换为实体
    LeshuaIsvKeyConfig toEntity(LeshuaIsvKeyConfigParam param);

    /// 更新源数据到实体(空值不覆盖, 密钥为空时保留原值)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(LeshuaIsvKeyConfigParam param, @MappingTarget LeshuaIsvKeyConfig entity);
}
