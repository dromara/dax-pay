package org.dromara.daxpay.payment.merchant.dao.profile;

import org.dromara.daxpay.payment.merchant.entity.profile.MchCardHolderProfile;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 商户持卡人信息
///
@Mapper
public interface MchCardHolderProfileMapper extends MPJBaseMapper<MchCardHolderProfile> {
}