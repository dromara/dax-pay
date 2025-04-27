package org.dromara.daxpay.service.service.notice.callback;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.service.dao.notice.callback.MerchantCallbackRecordManager;
import org.dromara.daxpay.service.dao.notice.callback.MerchantCallbackTaskManager;
import org.dromara.daxpay.service.entity.notice.callback.MerchantCallbackRecord;
import org.dromara.daxpay.service.entity.notice.callback.MerchantCallbackTask;
import org.dromara.daxpay.service.param.notice.callback.MerchantCallbackTaskQuery;
import org.dromara.daxpay.service.result.notice.callback.MerchantCallbackRecordResult;
import org.dromara.daxpay.service.result.notice.callback.MerchantCallbackTaskResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 商户回调查询服务
 * @author xxm
 * @since 2024/8/5
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantCallbackQueryService {
    private final MerchantCallbackTaskManager taskManager;

    private final MerchantCallbackRecordManager recordService;

    /**
     * 分页查询
     */
    public PageResult<MerchantCallbackTaskResult> page(PageParam param, MerchantCallbackTaskQuery query){
        return MpUtil.toPageResult(taskManager.page(param,query));
    }

    /**
     * 查询详情
     */
    public MerchantCallbackTaskResult findById(Long id){
        return taskManager.findById(id)
                .map(MerchantCallbackTask::toResult).orElseThrow(() -> new DataNotExistException("商户回调通知任务不存在"));
    }

    /**
     * 明细列表分页
     */
    public PageResult<MerchantCallbackRecordResult> pageRecord(PageParam param, Long taskId){
        return MpUtil.toPageResult(recordService.page(param ,taskId));

    }

    /**
     * 查询详细记录内容
     */
    public MerchantCallbackRecordResult findRecordById(Long id){
        return recordService.findById(id)
                .map(MerchantCallbackRecord::toResult)
                .orElseThrow(() -> new DataNotExistException("商户订阅通知记录不存在"));
    }
}
