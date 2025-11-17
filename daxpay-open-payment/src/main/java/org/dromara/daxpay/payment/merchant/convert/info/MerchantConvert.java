package org.dromara.daxpay.payment.merchant.convert.info;

import cn.bootx.platform.iam.param.user.UserInfoParam;
import org.dromara.daxpay.payment.merchant.entity.info.Merchant;
import org.dromara.daxpay.payment.merchant.param.info.MerchantParam;
import org.dromara.daxpay.payment.merchant.param.info.MerchantRegisterParam;
import org.dromara.daxpay.payment.merchant.result.info.MerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * 商户转换
 * @author xxm
 * @since 2024/6/24
 */
@Mapper
public interface MerchantConvert {
    MerchantConvert CONVERT = Mappers.getMapper(MerchantConvert.class);

    Merchant toEntity(MerchantParam param);

    MerchantResult toResult(Merchant entity);

    Merchant toEntity(MerchantRegisterParam param);

    void copy(MerchantRegisterParam param, @MappingTarget UserInfoParam userInfoParam);
}
