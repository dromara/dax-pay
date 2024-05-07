package cn.bootx.platform.daxpay.service.core.record.callback.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.service.common.context.CallbackLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.record.callback.dao.PayCallbackRecordManager;
import cn.bootx.platform.daxpay.service.core.record.callback.entity.PayCallbackRecord;
import cn.bootx.platform.daxpay.service.dto.record.callback.PayCallbackRecordDto;
import cn.bootx.platform.daxpay.service.param.record.PayCallbackRecordQuery;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 接收到的支付回调通知
 * @author xxm
 * @since 2023/12/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayCallbackRecordService {
    private final PayCallbackRecordManager callbackRecordManager;

    /**
     * 根据id查询
     */
    public PayCallbackRecordDto findById(Long id) {
        return callbackRecordManager.findById(id).map(PayCallbackRecord::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 分页查询
     */
    public PageResult<PayCallbackRecordDto> page(PageParam pageParam, PayCallbackRecordQuery param){
        return MpUtil.convert2DtoPageResult(callbackRecordManager.page(pageParam,param));
    }

    /**
     * 保存回调记录
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveCallbackRecord() {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        PayCallbackRecord payNotifyRecord = new PayCallbackRecord()
                .setTradeNo(callbackInfo.getTradeNo())
                .setOutTradeNo(callbackInfo.getOutTradeNo())
                .setChannel(callbackInfo.getChannel())
                .setNotifyInfo(JSONUtil.toJsonStr(callbackInfo.getCallbackParam()))
                .setCallbackType(callbackInfo.getCallbackType().getCode())
                .setRepairOrderNo(callbackInfo.getRepairNo())
                .setStatus(callbackInfo.getCallbackStatus().getCode())
                .setErrorMsg(callbackInfo.getErrorMsg());
        callbackRecordManager.save(payNotifyRecord);
    }
}
