package org.dromara.daxpay.payment.pay.service.record.close;

import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.exception.operation.OperationFailException;

import org.dromara.daxpay.payment.pay.dao.record.close.PayCloseRecordManager;
import org.dromara.daxpay.payment.pay.entity.record.close.PayCloseRecord;
import org.dromara.daxpay.payment.pay.param.record.PayCloseRecordQuery;
import org.dromara.daxpay.payment.pay.result.record.close.PayCloseRecordResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.dromara.daxpay.platform.core.code.CommonCode;

/// # 支付关闭记录
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PayCloseRecordService {
    private final PayCloseRecordManager manager;

    /// 根据id查询
    public PayCloseRecordResult findById(Long id) {
        return manager.findById(id).map(PayCloseRecord::toResult).orElseThrow(DataNotExistException::new);
    }

    /// 分页查询
    public PageResult<PayCloseRecordResult> page(PageParam pageParam, PayCloseRecordQuery param){
        return MpUtil.toPageResult(manager.page(pageParam,param));
    }

    /// 新开事务进行记录保存
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveRecord(PayCloseRecord record){
        manager.save(record);
    }
}
