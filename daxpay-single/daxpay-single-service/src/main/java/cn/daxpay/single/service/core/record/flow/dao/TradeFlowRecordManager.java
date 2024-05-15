package cn.daxpay.single.service.core.record.flow.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.single.service.core.record.flow.entity.TradeFlowRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/4/21
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class TradeFlowRecordManager extends BaseManager<TradeFlowRecordMapper, TradeFlowRecord> {
}
