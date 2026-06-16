package org.dromara.daxpay.payment.pay.order.dao;

import org.dromara.daxpay.payment.pay.order.entity.PayNormalOrder;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 普通支付业务单 Mapper
///
@Mapper
public interface PayNormalOrderMapper extends MPJBaseMapper<PayNormalOrder> {
}
