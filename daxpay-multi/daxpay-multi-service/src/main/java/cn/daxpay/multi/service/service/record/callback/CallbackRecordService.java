package cn.daxpay.multi.service.service.record.callback;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.daxpay.multi.service.common.context.CallbackLocal;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import cn.daxpay.multi.service.dao.record.callback.CallbackRecordManager;
import cn.daxpay.multi.service.entity.record.callback.CallbackRecord;
import cn.daxpay.multi.service.param.record.CallbackRecordQuery;
import cn.daxpay.multi.service.result.record.callback.CallbackRecordResult;
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
public class CallbackRecordService {
    private final CallbackRecordManager callbackRecordManager;

    /**
     * 根据id查询
     */
    public CallbackRecordResult findById(Long id) {
        return callbackRecordManager.findById(id).map(CallbackRecord::toResult).orElseThrow(DataNotExistException::new);
    }

    /**
     * 分页查询
     */
    public PageResult<CallbackRecordResult> page(PageParam pageParam, CallbackRecordQuery param){
        return MpUtil.toPageResult(callbackRecordManager.page(pageParam,param));
    }

    /**
     * 保存回调记录
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveCallbackRecord() {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        CallbackRecord payNotifyRecord = new CallbackRecord()
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
