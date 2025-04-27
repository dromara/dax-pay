package org.dromara.daxpay.controller.allocation;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.param.allocation.receiver.AllocReceiverQuery;
import org.dromara.daxpay.service.result.allocation.receiver.AllocReceiverVo;
import org.dromara.daxpay.service.service.allocation.receiver.AllocReceiverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分账接收方控制器
 * @author xxm
 * @since 2024/3/28
 */
@Validated
@Tag(name = "分账接收方控制器")
@RestController
@RequestGroup(groupCode = "AllocReceiver", groupName = "分账接收方", moduleCode = "Alloc")
@RequestMapping("/allocation/receiver")
@RequiredArgsConstructor
public class AllocReceiverController {

    private final AllocReceiverService receiverService;

    @RequestPath("分页")
    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<AllocReceiverVo>> page(PageParam pageParam, AllocReceiverQuery query){
        return Res.ok(receiverService.page(pageParam, query));
    }

    @RequestPath("查询详情")
    @Operation(summary = "查询详情")
    @GetMapping("/findById")
    public Result<AllocReceiverVo> findById(@NotNull(message = "主键不可为空") Long id){
        return Res.ok(receiverService.findById(id));
    }

    @RequestPath("删除")
    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(Long id){
        receiverService.remove(id);
        return Res.ok();
    }

    @RequestPath("可分账的通道列表")
    @Operation(summary = "可分账的通道列表")
    @GetMapping("/findChannels")
    public Result<List<LabelValue>> findChannels(){
        return Res.ok(receiverService.findChannels());
    }

}
