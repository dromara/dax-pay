package cn.bootx.platform.daxpay.service.core.record.sync.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.service.core.record.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.record.sync.dao.PaySyncRecordManager;
import cn.bootx.platform.daxpay.service.core.record.sync.entity.PaySyncRecord;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.GatewaySyncResult;
import cn.bootx.platform.daxpay.service.dto.order.sync.PaySyncRecordDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 支付同步记录
 * @author xxm
 * @since 2023/7/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaySyncOrderService {
    private final PaySyncRecordManager orderManager;

    /**
     * 记录同步记录
     */
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void saveRecord(GatewaySyncResult paySyncResult, PayOrder payment){
        PaySyncRecord paySyncRecord = new PaySyncRecord()
                .setPaymentId(payment.getId())
                .setChannel(payment.getAsyncChannel())
                .setSyncInfo(paySyncResult.getJson())
                .setStatus(paySyncResult.getSyncStatus().getCode())
                .setMsg(paySyncResult.getMsg())
                .setSyncTime(LocalDateTime.now());
        orderManager.save(paySyncRecord);
    }

    /**
     * 分页查询
     */
    public PageResult<PaySyncRecordDto> page(PageParam pageParam, PaySyncRecordDto param) {
        Page<PaySyncRecord> page = orderManager.page(pageParam, param);
        return MpUtil.convert2DtoPageResult(page);
    }

    /**
     * 根据id查询
     */
    public PaySyncRecordDto findById(Long id) {
        return orderManager.findById(id).map(PaySyncRecord::toDto).orElseThrow(DataNotExistException::new);
    }
}
