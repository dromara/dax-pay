package org.dromara.daxpay.payment.old.pay.dao.masterdata.capability;

import org.dromara.daxpay.payment.old.pay.entity.masterdata.product.PayProductCapability;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付产品能力关联 Mapper
@Mapper
public interface PayProductCapabilityMapper extends MPJBaseMapper<PayProductCapability> {
}