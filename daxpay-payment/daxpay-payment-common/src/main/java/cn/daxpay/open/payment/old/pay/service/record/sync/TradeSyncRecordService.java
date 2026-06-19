package cn.daxpay.open.payment.old.pay.service.record.sync;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.code.CommonCode;

import cn.daxpay.open.payment.old.pay.dao.record.sync.TradeSyncRecordManager;
import cn.daxpay.open.payment.old.pay.entity.record.sync.TradeSyncRecord;
import cn.daxpay.open.payment.old.pay.param.record.TradeSyncRecordQuery;
import cn.daxpay.open.payment.old.pay.result.record.sync.TradeSyncRecordResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/// # 交易同步记录, 包括支付/退款/分账
///
@Slf4j
@Service
@RequiredArgsConstructor
public class TradeSyncRecordService {

    private final TradeSyncRecordManager orderManager;

    /// 分页查询
    public PageResult<TradeSyncRecordResult> page(PageParam pageParam, TradeSyncRecordQuery query) {
        Page<TradeSyncRecord> page = orderManager.page(pageParam, query);
        return MpUtil.toPageResult(page);
    }

    /// 根据id查询
    public TradeSyncRecordResult findById(Long id) {
        return orderManager.findById(id).map(TradeSyncRecord::toResult).orElseThrow(DataNotExistException::new);
    }

    /// 记录交易同步记录
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveRecord(TradeSyncRecord tradeSyncRecord){
        orderManager.save(tradeSyncRecord);
    }

    
    

}
