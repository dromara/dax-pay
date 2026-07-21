package cn.daxpay.open.payment.trade.record.dao;

import cn.daxpay.open.payment.trade.record.entity.PayCallbackRecord;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 通道入站回调记录 Mapper
///
@Mapper
public interface PayCallbackRecordMapper extends MPJBaseMapper<PayCallbackRecord> {
}
