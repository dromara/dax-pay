package cn.daxpay.open.payment.douyin.convert.merchant;

import cn.daxpay.open.payment.douyin.entity.merchant.DyMchApp;
import cn.daxpay.open.payment.douyin.param.merchant.DyMchAppParam;
import cn.daxpay.open.payment.douyin.result.merchant.DyMchAppResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 商户抖音应用转换
///
@Mapper
public interface DyMchAppConvert {

    DyMchAppConvert CONVERT = Mappers.getMapper(DyMchAppConvert.class);

    /// 转换为返回对象
    DyMchAppResult toResult(DyMchApp entity);

    /// 转换为实体
    DyMchApp toEntity(DyMchAppParam param);

    /// 更新源数据到实体(空值不覆盖)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(DyMchAppParam param, @MappingTarget DyMchApp entity);
}
