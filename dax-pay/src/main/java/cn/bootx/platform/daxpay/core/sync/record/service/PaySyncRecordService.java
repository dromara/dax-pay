package cn.bootx.platform.daxpay.core.sync.record.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.core.payment.entity.Payment;
import cn.bootx.platform.daxpay.core.sync.record.dao.PaySyncRecordManager;
import cn.bootx.platform.daxpay.core.sync.record.entity.PaySyncRecord;
import cn.bootx.platform.daxpay.core.sync.result.PaySyncResult;
import cn.bootx.platform.daxpay.dto.sync.PaySyncRecordDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 支付同步记录
 * @author xxm
 * @since 2023/7/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaySyncRecordService {
    private final PaySyncRecordManager syncRecordManager;

    /**
     * 记录同步记录
     */
    public void saveRecord(PaySyncResult paySyncResult, Payment payment){
        PaySyncRecord paySyncRecord = new PaySyncRecord()
                .setPaymentId(payment.getId())
                .setMchCode(payment.getMchCode())
                .setMchAppCode(payment.getMchAppCode())
                .setPayChannel(payment.getAsyncPayChannel())
                .setSyncInfo(paySyncResult.getJson())
                .setStatus(paySyncResult.getPaySyncStatus())
                .setSyncTime(LocalDateTime.now());
        syncRecordManager.save(paySyncRecord);
    }

    /**
     * 分页查询
     */
    public PageResult<PaySyncRecordDto> page(PageParam pageParam, PaySyncRecordDto param) {
        Page<PaySyncRecord> page = syncRecordManager.page(pageParam, param);
        return MpUtil.convert2DtoPageResult(page);
    }

    /**
     * 根据id查询
     */
    public PaySyncRecordDto findById(Long id) {
        return syncRecordManager.findById(id).map(PaySyncRecord::toDto).orElseThrow(DataNotExistException::new);
    }
}
