package org.dromara.daxpay.service.service.notice.notify;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.service.dao.notice.notify.MerchantNotifyRecordManager;
import org.dromara.daxpay.service.dao.notice.notify.MerchantNotifyTaskManager;
import org.dromara.daxpay.service.entity.notice.notify.MerchantNotifyRecord;
import org.dromara.daxpay.service.entity.notice.notify.MerchantNotifyTask;
import org.dromara.daxpay.service.param.notice.notify.MerchantNotifyTaskQuery;
import org.dromara.daxpay.service.result.notice.notify.MerchantNotifyRecordResult;
import org.dromara.daxpay.service.result.notice.notify.MerchantNotifyTaskResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 商户订阅通知查询服务
 * @author xxm
 * @since 2024/8/5
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantNotifyQueryService {
    private final MerchantNotifyTaskManager taskManager;

    private final MerchantNotifyRecordManager recordService;

    /**
     * 分页查询
     */
    public PageResult<MerchantNotifyTaskResult> page(PageParam param, MerchantNotifyTaskQuery query){
        return MpUtil.toPageResult(taskManager.page(param,query));
    }

    /**
     * 查询详情
     */
    public MerchantNotifyTaskResult findById(Long id){
        return taskManager.findById(id)
                .map(MerchantNotifyTask::toResult).orElseThrow(() -> new DataNotExistException("商户订阅通知任务不存在"));
    }

    /**
     * 明细列表分页
     */
    public PageResult<MerchantNotifyRecordResult> pageRecord(PageParam param, Long taskId){
        return MpUtil.toPageResult(recordService.page(param ,taskId));

    }

    /**
     * 查询详细记录内容
     */
    public MerchantNotifyRecordResult findRecordById(Long id){
        return recordService.findById(id)
                .map(MerchantNotifyRecord::toResult)
                .orElseThrow(() -> new DataNotExistException("商户订阅通知记录不存在"));
    }

}
