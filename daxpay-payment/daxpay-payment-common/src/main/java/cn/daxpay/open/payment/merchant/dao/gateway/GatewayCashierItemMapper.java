package cn.daxpay.open.payment.merchant.dao.gateway;

import cn.daxpay.open.payment.merchant.entity.gateway.GatewayCashierItem;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 网关收银台支付项 Mapper
@Mapper
public interface GatewayCashierItemMapper extends MPJBaseMapper<GatewayCashierItem> {
}
