package cn.daxpay.multi.service.dao.record;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.multi.service.entity.record.flow.TradeFlowRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 交易流水
 * @author xxm
 * @since 2024/6/30
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class TradeFlowRecordManager extends BaseManager<TradeFlowRecordMapper, TradeFlowRecord> {
}
