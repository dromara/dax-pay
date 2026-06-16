package org.dromara.daxpay.payment.old.pay.dao.record.sync;

import org.dromara.daxpay.payment.old.pay.entity.record.sync.TradeSyncRecord;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付同步记录
///
@Mapper
public interface TradeSyncRecordMapper extends MPJBaseMapper<TradeSyncRecord> {
}
