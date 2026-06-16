package org.dromara.daxpay.payment.masterdata.constants.provider.dao;

import org.dromara.daxpay.payment.masterdata.constants.provider.entity.PayProvider;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付渠道
@Mapper
public interface PayProviderMapper extends MPJBaseMapper<PayProvider> {
}