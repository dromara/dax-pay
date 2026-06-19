package cn.daxpay.open.payment.masterdata.constants.capability.dao;

import cn.daxpay.open.payment.masterdata.constants.product.entity.PayProductCapability;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付产品能力关联 Mapper
@Mapper
public interface PayProductCapabilityMapper extends MPJBaseMapper<PayProductCapability> {
}