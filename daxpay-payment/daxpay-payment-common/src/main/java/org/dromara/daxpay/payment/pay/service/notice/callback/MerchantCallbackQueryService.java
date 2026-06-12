package org.dromara.daxpay.payment.pay.service.notice.callback;

import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.exception.operation.OperationFailException;

import org.dromara.daxpay.payment.pay.dao.notice.callback.MerchantCallbackRecordManager;
import org.dromara.daxpay.payment.pay.dao.notice.callback.MerchantCallbackTaskManager;
import org.dromara.daxpay.payment.pay.entity.notice.callback.MerchantCallbackRecord;
import org.dromara.daxpay.payment.pay.entity.notice.callback.MerchantCallbackTask;
import org.dromara.daxpay.payment.pay.param.notice.callback.MerchantCallbackTaskQuery;
import org.dromara.daxpay.payment.pay.result.notice.callback.MerchantCallbackRecordResult;
import org.dromara.daxpay.payment.pay.result.notice.callback.MerchantCallbackTaskResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.dromara.daxpay.platform.core.code.CommonCode;

/// # 商户回调查询服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantCallbackQueryService {
    private final MerchantCallbackTaskManager taskManager;

    private final MerchantCallbackRecordManager recordService;


    /// 分页查询
    public PageResult<MerchantCallbackTaskResult> page(PageParam param, MerchantCallbackTaskQuery query){
        return MpUtil.toPageResult(taskManager.page(param,query));
    }

    /// 查询详情
    public MerchantCallbackTaskResult findById(Long id){
        return taskManager.findById(id)
                .map(MerchantCallbackTask::toResult).orElseThrow(() -> new DataNotExistException("error.payment.order.callbackTaskNotExist"));
    }

    /// 明细列表分页
    public PageResult<MerchantCallbackRecordResult> pageRecord(PageParam param, Long taskId){
        return MpUtil.toPageResult(recordService.page(param ,taskId));

    }

    /// 查询详细记录内容
    public MerchantCallbackRecordResult findRecordById(Long id){
        return recordService.findById(id)
                .map(MerchantCallbackRecord::toResult)
                .orElseThrow(() -> new DataNotExistException("error.payment.order.subscribeRecordNotExist"));
    }

    
    
}
