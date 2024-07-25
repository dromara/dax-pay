package cn.daxpay.multi.service.service.record.callback;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.daxpay.multi.service.common.context.CallbackLocal;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import cn.daxpay.multi.service.dao.record.callback.TradeCallbackRecordManager;
import cn.daxpay.multi.service.entity.record.callback.TradeCallbackRecord;
import cn.daxpay.multi.service.param.record.TradeCallbackRecordQuery;
import cn.daxpay.multi.service.result.record.callback.TradeCallbackRecordResult;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
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
                .setNotifyInfo(JSONUtil.toJsonStr(callbackInfo.getCallbackData()))
                .setCallbackType(callbackInfo.getCallbackType().getCode())
                .setStatus(callbackInfo.getCallbackStatus().getCode())
                .setErrorCode(callbackInfo.getErrorCode())
                .setErrorMsg(callbackInfo.getErrorMsg());
        callbackRecordManager.save(payNotifyRecord);
    }
}
