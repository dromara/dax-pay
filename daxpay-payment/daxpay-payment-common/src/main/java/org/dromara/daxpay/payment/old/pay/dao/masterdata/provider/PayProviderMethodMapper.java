package org.dromara.daxpay.payment.old.pay.dao.masterdata.provider;

import org.dromara.daxpay.payment.old.pay.entity.masterdata.provider.PayProviderMethod;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 渠道支付方式目录项
@Mapper
public interface PayProviderMethodMapper extends MPJBaseMapper<PayProviderMethod> {
}