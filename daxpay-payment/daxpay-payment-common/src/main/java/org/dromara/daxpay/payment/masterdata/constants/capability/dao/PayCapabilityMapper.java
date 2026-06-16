package org.dromara.daxpay.payment.masterdata.constants.capability.dao;

import org.dromara.daxpay.payment.masterdata.constants.capability.entity.PayCapability;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付能力 Mapper
@Mapper
public interface PayCapabilityMapper extends MPJBaseMapper<PayCapability> {
}