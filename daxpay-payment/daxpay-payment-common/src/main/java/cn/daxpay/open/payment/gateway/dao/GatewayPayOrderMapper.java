package cn.daxpay.open.payment.gateway.dao;

import cn.daxpay.open.payment.gateway.entity.GatewayPayOrder;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 网关支付业务单 Mapper
@Mapper
public interface GatewayPayOrderMapper extends MPJBaseMapper<GatewayPayOrder> {
}
