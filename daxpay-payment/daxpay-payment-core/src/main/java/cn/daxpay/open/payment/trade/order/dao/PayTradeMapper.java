package cn.daxpay.open.payment.trade.order.dao;

import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 资金交易凭证 Mapper
///
@Mapper
public interface PayTradeMapper extends MPJBaseMapper<PayTrade> {
}
