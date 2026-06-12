package org.dromara.daxpay.payment.pay.dao.masterdata.method;

import org.dromara.daxpay.payment.pay.entity.masterdata.method.PayMethod;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付方式
@Mapper
public interface PayMethodMapper extends MPJBaseMapper<PayMethod> {
}