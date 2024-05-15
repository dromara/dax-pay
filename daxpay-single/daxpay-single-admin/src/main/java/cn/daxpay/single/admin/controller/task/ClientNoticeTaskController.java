package cn.daxpay.single.admin.controller.task;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.daxpay.single.service.core.task.notice.service.ClientNoticeTaskService;
import cn.daxpay.single.service.dto.record.notice.ClientNoticeRecordDto;
import cn.daxpay.single.service.dto.record.notice.ClientNoticeTaskDto;
import cn.daxpay.single.service.param.record.ClientNoticeRecordQuery;
import cn.daxpay.single.service.param.record.ClientNoticeTaskQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户通知任务
 * @author xxm
 * @since 2024/2/24
 */
@Tag(name = "客户系统通知任务")
@RestController
@RequestMapping("/task/notice")
@RequiredArgsConstructor
public class ClientNoticeTaskController {
    private final ClientNoticeTaskService clientNoticeTaskService;


    @Operation(summary = "重新发送消息通知")
    @PostMapping("/resetSend")
    public ResResult<Void> resetSend(Long taskId){
        clientNoticeTaskService.sendData(taskId);
        return Res.ok();
    }

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public ResResult<PageResult<ClientNoticeTaskDto>> page(PageParam pageParam, ClientNoticeTaskQuery query){
        return Res.ok(clientNoticeTaskService.taskPage(pageParam, query));
    }

    @Operation(summary = "查询单条")
    @GetMapping("/findById")
    public ResResult<ClientNoticeTaskDto> findById(Long id){
        return Res.ok(clientNoticeTaskService.findTaskById(id));
    }
    @Operation(summary = "分页查询")
    @GetMapping("/record/page")
    public ResResult<PageResult<ClientNoticeRecordDto>> recordPage(PageParam pageParam, ClientNoticeRecordQuery query){
        return Res.ok(clientNoticeTaskService.recordPage(pageParam, query));
    }

    @Operation(summary = "查询单条")
    @GetMapping("/record/findById")
    public ResResult<ClientNoticeRecordDto> findRecordById(Long id){
        return Res.ok(clientNoticeTaskService.findRecordById(id));
    }


}
