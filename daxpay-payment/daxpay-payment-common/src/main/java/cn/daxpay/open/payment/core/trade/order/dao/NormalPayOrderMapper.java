package cn.daxpay.open.payment.core.trade.order.dao;

import cn.daxpay.open.payment.core.trade.order.entity.NormalPayOrder;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 普通支付业务单 Mapper
///
@Mapper
public interface NormalPayOrderMapper extends MPJBaseMapper<NormalPayOrder> {
}
