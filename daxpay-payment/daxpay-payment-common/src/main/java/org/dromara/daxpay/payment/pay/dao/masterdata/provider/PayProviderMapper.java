package org.dromara.daxpay.payment.pay.dao.masterdata.provider;

import org.dromara.daxpay.payment.pay.entity.masterdata.provider.PayProvider;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付渠道
@Mapper
public interface PayProviderMapper extends MPJBaseMapper<PayProvider> {
}