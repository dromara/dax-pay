package cn.daxpay.open.payment.old.pay.service.record.callback;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.common.json.util.JsonUtil;

import cn.daxpay.open.payment.common.context.CallbackInfo;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeTypeEnum;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.old.pay.dao.record.callback.TradeCallbackRecordManager;
import cn.daxpay.open.payment.old.pay.entity.record.callback.TradeCallbackRecord;
import cn.daxpay.open.payment.old.pay.param.record.TradeCallbackRecordQuery;
import cn.daxpay.open.payment.old.pay.result.record.callback.TradeCallbackRecordResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/// # 交易回调记录服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class TradeCallbackRecordService {
    private final TradeCallbackRecordManager callbackRecordManager;

    private final PaymentContext apiContext;

    /// 根据id查询
    public TradeCallbackRecordResult findById(Long id) {
        return callbackRecordManager.findById(id).map(TradeCallbackRecord::toResult).orElseThrow(DataNotExistException::new);
    }

    /// 分页查询
    public PageResult<TradeCallbackRecordResult> page(PageParam pageParam, TradeCallbackRecordQuery param){
        return MpUtil.toPageResult(callbackRecordManager.page(pageParam,param));
    }

    /// 保存回调记录
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveCallbackRecord(String channel, String product, TradeTypeEnum callbackType) {
        CallbackInfo callbackInfo = apiContext.getCallbackInfo();
        TradeCallbackRecord payNotifyRecord = new TradeCallbackRecord()
                .setTradeNo(callbackInfo.getTradeNo())
                .setOutTradeNo(callbackInfo.getOutTradeNo())
                .setChannel(channel)
                .setProduct(product)
                .setNotifyInfo(JsonUtil.toJsonStr(callbackInfo.getCallbackData()))
                .setCallbackType(callbackType.getCode())
                .setStatus(callbackInfo.getCallbackStatus().getCode())
                .setErrorMsg(callbackInfo.getCallbackErrorMsg());
        callbackRecordManager.save(payNotifyRecord);
    }

    
    
}
