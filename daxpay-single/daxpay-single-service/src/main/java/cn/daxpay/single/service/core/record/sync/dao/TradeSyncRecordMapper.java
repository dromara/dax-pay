package cn.daxpay.single.service.core.record.sync.dao;

import cn.daxpay.single.service.core.record.sync.entity.TradeSyncRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付同步记录
 * @author xxm
 * @since 2023/7/14
 */
@Mapper
public interface TradeSyncRecordMapper extends BaseMapper<TradeSyncRecord> {
}
