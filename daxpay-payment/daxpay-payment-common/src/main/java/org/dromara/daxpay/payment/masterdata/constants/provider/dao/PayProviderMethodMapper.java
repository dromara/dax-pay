package org.dromara.daxpay.payment.masterdata.constants.provider.dao;

import org.dromara.daxpay.payment.masterdata.constants.provider.entity.PayProviderMethod;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 渠道支付方式目录项
@Mapper
public interface PayProviderMethodMapper extends MPJBaseMapper<PayProviderMethod> {
}