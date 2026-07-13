package cn.daxpay.open.payment.masterdata.dao.capability;

import cn.daxpay.open.payment.masterdata.entity.product.PayProductCapability;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付产品能力关联 Mapper
@Mapper
public interface PayProductCapabilityMapper extends MPJBaseMapper<PayProductCapability> {
}