package org.dromara.daxpay.payment.old.pay.dao.masterdata.provider;

import org.dromara.daxpay.payment.old.pay.entity.masterdata.provider.PayProvider;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付渠道
@Mapper
public interface PayProviderMapper extends MPJBaseMapper<PayProvider> {
}