package org.dromara.daxpay.service.controller.allocation;

import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.service.param.allocation.receiver.AllocReceiverAddParam;
import org.dromara.daxpay.service.param.allocation.receiver.AllocReceiverQuery;
import org.dromara.daxpay.service.param.allocation.receiver.AllocReceiverRemoveParam;
import org.dromara.daxpay.service.result.allocation.AllocReceiverResult;
import org.dromara.daxpay.service.service.allocation.AllocReceiverService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分账接收方控制器
 * @author xxm
 * @since 2024/3/28
 */
@Validated
@Tag(name = "分账接收方控制器")
@RestController
@RequestMapping("/allocation/receiver")
@RequiredArgsConstructor
public class AllocReceiverController {

    private final AllocReceiverService receiverService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<AllocReceiverResult>> page(PageParam pageParam, AllocReceiverQuery query){
        return Res.ok(receiverService.page(pageParam, query));
    }

    @Operation(summary = "查询详情")
    @GetMapping("/findById")
    public Result<AllocReceiverResult> findById(Long id){
        return Res.ok(receiverService.findById(id));
    }

    @Operation(summary = "编码是否存在")
    @GetMapping("/existsByReceiverNo")
    public Result<Boolean> existsByReceiverNo(@NotBlank(message = "接收者编号必填") String receiverNo,@NotBlank(message = "商户应用ID必填") String appId){
        return Res.ok(receiverService.existsByReceiverNo(receiverNo, appId));
    }


    @Operation(summary = "添加")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated AllocReceiverAddParam param){
        receiverService.addAndSync(param);
        return Res.ok();
    }

    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody @Validated AllocReceiverRemoveParam param){
        receiverService.removeAndSync(param);
        return Res.ok();
    }

    @Operation(summary = "可分账的通道列表")
    @GetMapping("/findChannels")
    public Result<List<LabelValue>> findChannels(){
        return Res.ok(receiverService.findChannels());
    }

    @Operation(summary = "根据通道获取分账接收方类型")
    @GetMapping("/findReceiverTypeByChannel")
    public Result<List<LabelValue>> findReceiverTypeByChannel(String channel){
        return Res.ok(receiverService.findReceiverTypeByChannel(channel));
    }
}
