package cn.bootx.platform.daxpay.service.core.task.notice.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.service.core.task.notice.entity.ClientNoticeRecord;
import cn.bootx.platform.daxpay.service.core.task.notice.entity.ClientNoticeTask;
import cn.bootx.platform.daxpay.service.core.payment.notice.service.ClientNoticeService;
import cn.bootx.platform.daxpay.service.core.task.notice.dao.ClientNoticeRecordManager;
import cn.bootx.platform.daxpay.service.core.task.notice.dao.ClientNoticeTaskManager;
import cn.bootx.platform.daxpay.service.dto.record.notice.ClientNoticeRecordDto;
import cn.bootx.platform.daxpay.service.dto.record.notice.ClientNoticeTaskDto;
import cn.bootx.platform.daxpay.service.param.record.ClientNoticeRecordQuery;
import cn.bootx.platform.daxpay.service.param.record.ClientNoticeTaskQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 通知任务查询类
 * @author xxm
 * @since 2024/2/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientNoticeTaskService {

    private final ClientNoticeService clientNoticeService;

    private final ClientNoticeTaskManager taskManager;

    private final ClientNoticeRecordManager recordManager;


    /**
     * 手动触发消息发送
     * 1. 手动触发消息发送次数不会变更
     * 2. 会记录发送历史
     * 3. 不受次数限制, 也不受状态控制. 任务完成与否都可以推送
     */
    public void sendData(Long taskId){
        ClientNoticeTask task = taskManager.findById(taskId)
                .orElseThrow(() -> new DataNotExistException("任务不存在"));
        clientNoticeService.sendDataByManual(task);
    }

    /**
     * 任务分页查询
     */
    public PageResult<ClientNoticeTaskDto> taskPage(PageParam pageParam, ClientNoticeTaskQuery query){
        return MpUtil.convert2DtoPageResult(taskManager.page(pageParam, query));
    }

    /**
     * 任务详情
     */
    public ClientNoticeTaskDto findTaskById(Long id){
        return taskManager.findById(id).map(ClientNoticeTask::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 记录分页
     */
    public PageResult<ClientNoticeRecordDto> recordPage(PageParam pageParam, ClientNoticeRecordQuery query){
        return MpUtil.convert2DtoPageResult(recordManager.page(pageParam, query));
    }

    /**
     * 记录详情
     */
    public ClientNoticeRecordDto findRecordById(Long id){
        return recordManager.findById(id).map(ClientNoticeRecord::toDto).orElseThrow(DataNotExistException::new);
    }
}
