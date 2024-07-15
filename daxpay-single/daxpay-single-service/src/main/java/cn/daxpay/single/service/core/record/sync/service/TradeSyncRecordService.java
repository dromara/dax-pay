package cn.daxpay.single.service.core.record.sync.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.single.service.core.record.sync.dao.TradeSyncRecordManager;
import cn.daxpay.single.service.core.record.sync.entity.TradeSyncRecord;
import cn.daxpay.single.service.dto.record.sync.SyncRecordDto;
import cn.daxpay.single.service.param.record.TradeSyncRecordQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 支付同步记录, 包括支付/退款/分账
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
    public PageResult<SyncRecordDto> page(PageParam pageParam, TradeSyncRecordQuery query) {
        Page<TradeSyncRecord> page = orderManager.page(pageParam, query);
        return MpUtil.convert2DtoPageResult(page);
    }

    /**
     * 根据id查询
     */
    public SyncRecordDto findById(Long id) {
        return orderManager.findById(id).map(TradeSyncRecord::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 记录同步记录 同步支付单的不进行记录
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveRecord(TradeSyncRecord tradeSyncRecord){
        orderManager.save(tradeSyncRecord);
    }

}
