package cn.bootx.platform.daxpay.service.core.record.close.service;

import cn.bootx.platform.daxpay.service.core.record.close.dao.PayCloseRecordManager;
import cn.bootx.platform.daxpay.service.core.record.close.entity.PayCloseRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 支付关闭记录
 * @author xxm
 * @since 2024/1/4
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayCloseRecordService {
    private final PayCloseRecordManager manager;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveRecord(PayCloseRecord record){
        manager.save(record);
    }
}
