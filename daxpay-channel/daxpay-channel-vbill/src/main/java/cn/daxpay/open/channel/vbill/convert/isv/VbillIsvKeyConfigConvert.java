package cn.daxpay.open.channel.vbill.convert.isv;

import cn.daxpay.open.channel.vbill.entity.isv.VbillIsvKeyConfig;
import cn.daxpay.open.channel.vbill.param.isv.VbillIsvKeyConfigParam;
import cn.daxpay.open.channel.vbill.result.isv.VbillIsvKeyConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 随行付服务商密钥配置转换
@Mapper
public interface VbillIsvKeyConfigConvert {

    VbillIsvKeyConfigConvert CONVERT = Mappers.getMapper(VbillIsvKeyConfigConvert.class);

    /// 转换为返回对象
    VbillIsvKeyConfigResult toResult(VbillIsvKeyConfig entity);

    /// 转换为实体
    VbillIsvKeyConfig toEntity(VbillIsvKeyConfigParam param);

    /// 更新源数据到实体(空值不覆盖, 密钥为空时保留原值)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(VbillIsvKeyConfigParam param, @MappingTarget VbillIsvKeyConfig entity);
}
