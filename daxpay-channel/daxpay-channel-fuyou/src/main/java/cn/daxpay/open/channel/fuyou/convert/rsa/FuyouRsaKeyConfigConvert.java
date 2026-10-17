package cn.daxpay.open.channel.fuyou.convert.rsa;

import cn.daxpay.open.channel.fuyou.entity.rsa.FuyouRsaKeyConfig;
import cn.daxpay.open.channel.fuyou.param.rsa.FuyouRsaKeyConfigParam;
import cn.daxpay.open.channel.fuyou.result.rsa.FuyouRsaKeyConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 富友 RSA 密钥对配置转换
@Mapper
public interface FuyouRsaKeyConfigConvert {

    FuyouRsaKeyConfigConvert CONVERT = Mappers.getMapper(FuyouRsaKeyConfigConvert.class);

    /// 转换为返回对象
    FuyouRsaKeyConfigResult toResult(FuyouRsaKeyConfig entity);

    /// 更新源数据到实体(空值不覆盖, 密钥为空时保留原值)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(FuyouRsaKeyConfigParam param, @MappingTarget FuyouRsaKeyConfig entity);
}
