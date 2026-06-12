package org.dromara.daxpay.payment.merchant.dao.profile;

import org.dromara.daxpay.payment.merchant.entity.profile.MchBaseProfile;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 商户基础信息
///
@Mapper
public interface MchBaseProfileMapper extends MPJBaseMapper<MchBaseProfile> {
}