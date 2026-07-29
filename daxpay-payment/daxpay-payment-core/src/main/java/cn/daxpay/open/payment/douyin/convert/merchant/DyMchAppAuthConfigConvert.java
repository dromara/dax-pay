package cn.daxpay.open.payment.douyin.convert.merchant;

import cn.daxpay.open.payment.douyin.entity.merchant.DyMchAppAuthConfig;
import cn.daxpay.open.payment.douyin.param.merchant.DyMchAppAuthConfigParam;
import cn.daxpay.open.payment.douyin.result.merchant.DyMchAppAuthConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 商户抖音应用授权认证配置转换
///
@Mapper
public interface DyMchAppAuthConfigConvert {

    DyMchAppAuthConfigConvert CONVERT = Mappers.getMapper(DyMchAppAuthConfigConvert.class);

    /// 转换为返回对象
    DyMchAppAuthConfigResult toResult(DyMchAppAuthConfig entity);

    /// 转换为实体
    DyMchAppAuthConfig toEntity(DyMchAppAuthConfigParam param);

    /// 更新源数据到实体(空值不覆盖)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(DyMchAppAuthConfigParam param, @MappingTarget DyMchAppAuthConfig entity);
}
