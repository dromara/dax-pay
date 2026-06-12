package org.dromara.daxpay.payment.merchant.dao.profile;

import org.dromara.daxpay.payment.merchant.entity.profile.MchBankCardProfile;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 商户银行卡信息
///
@Mapper
public interface MchBankCardProfileMapper extends MPJBaseMapper<MchBankCardProfile> {
}