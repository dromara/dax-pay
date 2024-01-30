package cn.bootx.platform.daxpay.service.core.record.sync.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.service.core.record.sync.dao.PaySyncRecordManager;
import cn.bootx.platform.daxpay.service.core.record.sync.entity.PaySyncRecord;
import cn.bootx.platform.daxpay.service.dto.record.sync.PaySyncRecordDto;
import cn.bootx.platform.daxpay.service.param.record.PaySyncRecordQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 支付同步记录
 * @author xxm
 * @since 2023/7/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaySyncRecordService {
    private final PaySyncRecordManager orderManager;

    /**
     * 分页查询
     */
    public PageResult<PaySyncRecordDto> page(PageParam pageParam, PaySyncRecordQuery query) {
        Page<PaySyncRecord> page = orderManager.page(pageParam, query);
        return MpUtil.convert2DtoPageResult(page);
    }

    /**
     * 根据id查询
     */
    public PaySyncRecordDto findById(Long id) {
        return orderManager.findById(id).map(PaySyncRecord::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 记录同步记录 同步支付单的不进行记录
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveRecord(PaySyncRecord paySyncRecord){
        orderManager.save(paySyncRecord);
    }

}
