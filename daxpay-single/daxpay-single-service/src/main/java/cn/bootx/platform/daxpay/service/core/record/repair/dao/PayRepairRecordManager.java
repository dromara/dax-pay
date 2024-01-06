package cn.bootx.platform.daxpay.service.core.record.repair.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.core.record.repair.entity.PayRepairRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/1/6
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayRepairRecordManager extends BaseManager<PayRepairRecordMapper, PayRepairRecord> {
}
