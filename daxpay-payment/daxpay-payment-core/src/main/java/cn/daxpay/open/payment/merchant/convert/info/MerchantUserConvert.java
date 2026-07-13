package cn.daxpay.open.payment.merchant.convert.info;

import cn.daxpay.open.platform.iam.entity.user.UserExpandInfo;
import cn.daxpay.open.platform.iam.entity.user.UserInfo;
import cn.daxpay.open.payment.merchant.param.info.MerchantUserParam;
import cn.daxpay.open.payment.merchant.result.info.MerchantUserDetailResult;
import cn.daxpay.open.payment.merchant.result.info.MerchantUserResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 商户用户转换
///
@Mapper
public interface MerchantUserConvert {
    MerchantUserConvert CONVERT = Mappers.getMapper(MerchantUserConvert.class);

    UserInfo toEntity(MerchantUserParam param);

    MerchantUserResult toResult(UserInfo userInfo);

    MerchantUserDetailResult toDetailResult(UserInfo userInfo);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(MerchantUserParam param, @MappingTarget UserInfo userInfo);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(UserExpandInfo userExpandInfo, @MappingTarget MerchantUserDetailResult result);
}
