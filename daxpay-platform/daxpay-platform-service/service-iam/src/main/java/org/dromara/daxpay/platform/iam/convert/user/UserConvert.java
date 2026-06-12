package org.dromara.daxpay.platform.iam.convert.user;

import org.dromara.daxpay.platform.iam.entity.user.UserExpandInfo;
import org.dromara.daxpay.platform.iam.entity.user.UserInfo;
import org.dromara.daxpay.platform.iam.param.user.UserBaseInfoParam;
import org.dromara.daxpay.platform.iam.param.user.UserInfoParam;
import org.dromara.daxpay.platform.iam.result.user.UserInfoResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserConvert {

    UserConvert CONVERT = Mappers.getMapper(UserConvert.class);

    UserInfo convert(UserInfoParam in);

    UserInfoResult convert(UserInfo in);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void copy(UserInfoParam userInfoParam, @MappingTarget UserInfo userInfo);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void copy(UserBaseInfoParam param, @MappingTarget UserInfo userInfo);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void copy(UserBaseInfoParam param, @MappingTarget UserExpandInfo userExpandInfo);
}
