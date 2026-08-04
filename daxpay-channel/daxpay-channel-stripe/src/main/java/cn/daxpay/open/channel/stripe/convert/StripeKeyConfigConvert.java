package cn.daxpay.open.channel.stripe.convert;

import cn.daxpay.open.channel.stripe.entity.StripeKeyConfig;
import cn.daxpay.open.channel.stripe.param.StripeKeyConfigParam;
import cn.daxpay.open.channel.stripe.result.StripeKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # Stripe 密钥配置转换
///
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StripeKeyConfigConvert {
    StripeKeyConfigConvert CONVERT = Mappers.getMapper(StripeKeyConfigConvert.class);

    StripeKeyConfigResult toResult(StripeKeyConfig entity);

    void copy(StripeKeyConfigParam param, @MappingTarget StripeKeyConfig entity);
}
