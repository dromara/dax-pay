package cn.bootx.platform.daxpay.service.core.record.repair.service;

import cn.bootx.platform.daxpay.service.core.record.repair.dao.PayRepairRecordManager;
import cn.bootx.platform.daxpay.service.core.record.repair.entity.PayRepairRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author xxm
 * @since 2024/1/6
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRepairRecordService {
    private final PayRepairRecordManager repairRecordManager;

    /**
     * 保存记录
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveRecord(PayRepairRecord record){
        repairRecordManager.save(record);
    }
}
