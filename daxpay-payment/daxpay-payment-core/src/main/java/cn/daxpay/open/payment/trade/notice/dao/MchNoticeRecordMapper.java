package cn.daxpay.open.payment.trade.notice.dao;

import cn.daxpay.open.payment.trade.notice.entity.MchNoticeRecord;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 商户出站通知发送记录 Mapper
///
@Mapper
public interface MchNoticeRecordMapper extends MPJBaseMapper<MchNoticeRecord> {
}
