package cn.daxpay.open.payment.trade.order.dao;

import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 退款订单 Mapper
///
@Mapper
public interface RefundOrderMapper extends MPJBaseMapper<RefundOrder> {
}
