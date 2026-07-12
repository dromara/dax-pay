package cn.daxpay.open.payment.core.trade.order.dao;

import cn.daxpay.open.payment.core.trade.order.entity.PayTrade;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 资金交易凭证 Mapper
///
@Mapper
public interface PayTradeMapper extends MPJBaseMapper<PayTrade> {
}
