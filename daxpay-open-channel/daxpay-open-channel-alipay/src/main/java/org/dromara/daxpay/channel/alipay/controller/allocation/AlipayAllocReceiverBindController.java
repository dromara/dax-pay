package org.dromara.daxpay.channel.alipay.controller.allocation;

import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.channel.alipay.param.allocation.AlipayAllocReceiverBindParam;
import org.dromara.daxpay.channel.alipay.param.allocation.AlipayAllocReceiverBindQuery;
import org.dromara.daxpay.channel.alipay.result.allocation.AlipayAllocReceiverBindResult;
import org.dromara.daxpay.channel.alipay.service.allocation.AlipayAllocReceiverBindService;
import org.dromara.core.trans.anno.TransMethodResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 支付宝分账接收方绑定
 * @author xxm
 * @since 2025/1/27
 */
@Validated
@Tag(name = "支付宝分账接收方绑定")
@RestController
@RequestGroup(groupCode = "AlipayAllocReceiver", groupName = "支付宝分账接收方", moduleCode = "alipay")
@RequestMapping("/alipay/allocation/receiver/bind")
@RequiredArgsConstructor
public class AlipayAllocReceiverBindController {

    private final AlipayAllocReceiverBindService receiverBindService;

    @TransMethodResult
    @RequestPath("分页")
    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<AlipayAllocReceiverBindResult>> page(PageParam pageParam, AlipayAllocReceiverBindQuery param){
        return Res.ok(receiverBindService.page(pageParam,param));
    }

    @TransMethodResult
    @RequestPath("详情")
    @Operation(summary = "详情")
    @GetMapping("/findById")
    public Result<AlipayAllocReceiverBindResult> findById(@NotNull(message = "主键不可为空") Long id){
        return Res.ok(receiverBindService.findById(id));
    }

    @RequestPath("添加")
    @Operation(summary = "添加")
    @PostMapping("/add")
    @OperateLog(title = "添加支付宝分账接收方绑定", businessType = OperateLog.BusinessType.ADD, saveParam = true)
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) AlipayAllocReceiverBindParam param){
        receiverBindService.add(param);
        return Res.ok();
    }

    @RequestPath("编辑")
    @Operation(summary = "编辑")
    @PostMapping("/update")
    @OperateLog(title = "编辑支付宝分账接收方绑定", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) AlipayAllocReceiverBindParam param){
        receiverBindService.update(param);
        return Res.ok();
    }

    @RequestPath("绑定")
    @Operation(summary = "绑定")
    @PostMapping("/bind")
    @OperateLog(title = "绑定支付宝分账接收方绑定", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> bind(@NotNull(message = "主键不可为空") Long id){
        receiverBindService.bind(id);
        return Res.ok();
    }

    @RequestPath("解绑")
    @Operation(summary = "解绑")
    @PostMapping("/unbind")
    @OperateLog(title = "解绑支付宝分账接收方绑定", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> unbind(@NotNull(message = "主键不可为空") Long id){
        receiverBindService.unbind(id);
        return Res.ok();
    }

    @RequestPath("删除")
    @Operation(summary = "删除")
    @PostMapping("/remove")
    @OperateLog(title = "删除支付宝分账接收方绑定", businessType = OperateLog.BusinessType.DELETE, saveParam = true)
    public Result<Void> remove(@NotNull(message = "主键不可为空") Long id){
        receiverBindService.remove(id);
        return Res.ok();
    }
}
