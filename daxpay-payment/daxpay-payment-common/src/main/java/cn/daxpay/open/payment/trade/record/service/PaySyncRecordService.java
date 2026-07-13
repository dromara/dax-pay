package cn.daxpay.open.payment.trade.record.service;

import cn.daxpay.open.payment.trade.record.dao.PaySyncRecordManager;
import cn.daxpay.open.payment.trade.record.entity.PaySyncRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/// # 支付同步记录服务
///
/// 仅提供记录保存, 查询后续按需补充
@Slf4j
@Service
@RequiredArgsConstructor
public class PaySyncRecordService {

    private final PaySyncRecordManager manager;

    /// 新开事务保存同步记录, 不受外部事务回滚影响
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveRecord(PaySyncRecord record) {
        manager.save(record);
    }
}
