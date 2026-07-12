package cn.daxpay.open.payment.core.trade.order.dao;

import cn.daxpay.open.payment.core.trade.order.entity.PayRefundOrder;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 退款订单 Mapper
///
@Mapper
public interface PayRefundOrderMapper extends MPJBaseMapper<PayRefundOrder> {
}
