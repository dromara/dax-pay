package org.dromara.daxpay.service.service.record.sync;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.service.dao.record.sync.TradeSyncRecordManager;
import org.dromara.daxpay.service.entity.record.sync.TradeSyncRecord;
import org.dromara.daxpay.service.param.record.TradeSyncRecordQuery;
import org.dromara.daxpay.service.result.record.sync.TradeSyncRecordResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 交易同步记录, 包括支付/退款/分账
 * @author xxm
 * @since 2023/7/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TradeSyncRecordService {

    private final TradeSyncRecordManager orderManager;

    /**
     * 分页查询
     */
    public PageResult<TradeSyncRecordResult> page(PageParam pageParam, TradeSyncRecordQuery query) {
        Page<TradeSyncRecord> page = orderManager.page(pageParam, query);
        return MpUtil.toPageResult(page);
    }

    /**
     * 根据id查询
     */
    public TradeSyncRecordResult findById(Long id) {
        return orderManager.findById(id).map(TradeSyncRecord::toResult).orElseThrow(DataNotExistException::new);
    }

    /**
     * 记录交易同步记录
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveRecord(TradeSyncRecord tradeSyncRecord){
        orderManager.save(tradeSyncRecord);
    }

}
