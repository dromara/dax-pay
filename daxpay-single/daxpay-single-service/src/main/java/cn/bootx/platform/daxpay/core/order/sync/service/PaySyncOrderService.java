package cn.bootx.platform.daxpay.core.order.sync.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.core.order.sync.dao.PaySyncOrderManager;
import cn.bootx.platform.daxpay.core.order.sync.entity.PaySyncOrder;
import cn.bootx.platform.daxpay.core.payment.sync.result.SyncResult;
import cn.bootx.platform.daxpay.dto.order.sync.PaySyncOrderDto;
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
    private final PaySyncOrderManager orderManager;

    /**
     * 记录同步记录
     */
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void saveRecord(SyncResult paySyncResult, PayOrder payment){
        PaySyncOrder paySyncOrder = new PaySyncOrder()
                .setPaymentId(payment.getId())
                .setChannel(payment.getAsyncPayChannel())
                .setSyncInfo(paySyncResult.getJson())
                .setStatus(paySyncResult.getSyncStatus())
                .setMsg(paySyncResult.getMsg())
                .setSyncTime(LocalDateTime.now());
        orderManager.save(paySyncOrder);
    }

    /**
     * 分页查询
     */
    public PageResult<PaySyncOrderDto> page(PageParam pageParam, PaySyncOrderDto param) {
        Page<PaySyncOrder> page = orderManager.page(pageParam, param);
        return MpUtil.convert2DtoPageResult(page);
    }

    /**
     * 根据id查询
     */
    public PaySyncOrderDto findById(Long id) {
        return orderManager.findById(id).map(PaySyncOrder::toDto).orElseThrow(DataNotExistException::new);
    }
}
