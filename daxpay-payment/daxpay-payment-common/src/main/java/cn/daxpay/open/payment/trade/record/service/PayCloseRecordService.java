package cn.daxpay.open.payment.trade.record.service;

import cn.daxpay.open.payment.trade.record.dao.PayCloseRecordManager;
import cn.daxpay.open.payment.trade.record.entity.PayCloseRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/// # 支付关闭记录服务
///
/// 仅提供记录保存, 查询后续按需补充
@Slf4j
@Service
@RequiredArgsConstructor
public class PayCloseRecordService {

    private final PayCloseRecordManager manager;

    /// 新开事务保存关闭记录, 不受外部事务回滚影响
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveRecord(PayCloseRecord record) {
        manager.save(record);
    }
}
