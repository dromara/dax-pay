package org.dromara.daxpay.service.service.record.callback;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.common.context.CallbackLocal;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.record.callback.TradeCallbackRecordManager;
import org.dromara.daxpay.service.entity.record.callback.TradeCallbackRecord;
import org.dromara.daxpay.service.param.record.TradeCallbackRecordQuery;
import org.dromara.daxpay.service.result.record.callback.TradeCallbackRecordResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 交易回调记录
 * @author xxm
 * @since 2024/7/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TradeCallbackRecordService {
    private final TradeCallbackRecordManager callbackRecordManager;

    /**
     * 根据id查询
     */
    public TradeCallbackRecordResult findById(Long id) {
        return callbackRecordManager.findById(id).map(TradeCallbackRecord::toResult).orElseThrow(DataNotExistException::new);
    }

    /**
     * 分页查询
     */
    public PageResult<TradeCallbackRecordResult> page(PageParam pageParam, TradeCallbackRecordQuery param){
        return MpUtil.toPageResult(callbackRecordManager.page(pageParam,param));
    }

    /**
     * 保存回调记录
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveCallbackRecord() {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        TradeCallbackRecord payNotifyRecord = new TradeCallbackRecord()
                .setTradeNo(callbackInfo.getTradeNo())
                .setOutTradeNo(callbackInfo.getOutTradeNo())
                .setChannel(callbackInfo.getChannel())
                .setNotifyInfo(JsonUtil.toJsonStr(callbackInfo.getCallbackData()))
                .setCallbackType(callbackInfo.getCallbackType().getCode())
                .setStatus(callbackInfo.getCallbackStatus().getCode())
                .setErrorCode(callbackInfo.getCallbackErrorCode())
                .setErrorMsg(callbackInfo.getCallbackErrorMsg());
        callbackRecordManager.save(payNotifyRecord);
    }
}
