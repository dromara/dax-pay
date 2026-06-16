package org.dromara.daxpay.payment.masterdata.constants.method.dao;

import org.dromara.daxpay.payment.masterdata.constants.method.entity.PayMethod;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付方式
@Mapper
public interface PayMethodMapper extends MPJBaseMapper<PayMethod> {
}