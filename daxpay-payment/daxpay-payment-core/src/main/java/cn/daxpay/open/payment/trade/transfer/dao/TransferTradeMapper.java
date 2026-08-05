package cn.daxpay.open.payment.trade.transfer.dao;

import cn.daxpay.open.payment.trade.transfer.entity.TransferTrade;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 转账资金凭证 Mapper
///
@Mapper
public interface TransferTradeMapper extends MPJBaseMapper<TransferTrade> {
}
