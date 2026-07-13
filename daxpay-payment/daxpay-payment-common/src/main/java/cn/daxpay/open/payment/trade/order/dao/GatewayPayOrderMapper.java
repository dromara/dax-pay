package cn.daxpay.open.payment.trade.order.dao;

import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 网关支付业务单 Mapper
@Mapper
public interface GatewayPayOrderMapper extends MPJBaseMapper<GatewayPayOrder> {
}
