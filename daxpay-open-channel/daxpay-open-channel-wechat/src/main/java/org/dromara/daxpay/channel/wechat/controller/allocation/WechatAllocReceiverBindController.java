package org.dromara.daxpay.channel.wechat.controller.allocation;

import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.channel.wechat.param.allocation.WechatAllocReceiverBindParam;
import org.dromara.daxpay.channel.wechat.param.allocation.WechatAllocReceiverBindQuery;
import org.dromara.daxpay.channel.wechat.result.allocation.WechatAllocReceiverBindResult;
import org.dromara.daxpay.channel.wechat.service.allocation.WechatAllocReceiverBindService;
import org.dromara.core.trans.anno.TransMethodResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 微信分账接收方绑定
 * @author xxm
 * @since 2025/1/27
 */
@Validated
@Tag(name = "微信分账接收方绑定")
@RestController
@RequestGroup(groupCode = "WechatAllocReceiver", groupName = "微信分账接收方", moduleCode = "wechatPay")
@RequestMapping("/wechat/allocation/receiver/bind")
@RequiredArgsConstructor
public class WechatAllocReceiverBindController {

    private final WechatAllocReceiverBindService receiverBindService;

    @TransMethodResult
    @RequestPath("分页")
    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<WechatAllocReceiverBindResult>> page(PageParam pageParam, WechatAllocReceiverBindQuery param){
        return Res.ok(receiverBindService.page(pageParam,param));
    }

    @TransMethodResult
    @RequestPath("详情")
    @Operation(summary = "详情")
    @GetMapping("/findById")
    public Result<WechatAllocReceiverBindResult> findById(@NotNull(message = "主键不可为空") Long id){
        return Res.ok(receiverBindService.findById(id));
    }

    @RequestPath("添加")
    @Operation(summary = "添加")
    @PostMapping("/add")
    @OperateLog(title = "添加微信分账接收方绑定 ", businessType = OperateLog.BusinessType.ADD, saveParam = true)
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) WechatAllocReceiverBindParam param){
        receiverBindService.add(param);
        return Res.ok();
    }

    @RequestPath("编辑")
    @Operation(summary = "编辑")
    @PostMapping("/update")
    @OperateLog(title = "编辑支付宝分账接收方绑定", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) WechatAllocReceiverBindParam param){
        receiverBindService.update(param);
        return Res.ok();
    }

    @RequestPath("绑定")
    @Operation(summary = "绑定")
    @PostMapping("/bind")
    @OperateLog(title = "绑定微信分账接收方绑定", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> bind(@NotNull(message = "主键不可为空") Long id){
        receiverBindService.bind(id);
        return Res.ok();
    }

    @RequestPath("解绑")
    @Operation(summary = "解绑")
    @PostMapping("/unbind")
    @OperateLog(title = "解绑微信分账接收方绑定", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> unbind(@NotNull(message = "主键不可为空") Long id){
        receiverBindService.unbind(id);
        return Res.ok();
    }

    @RequestPath("删除")
    @Operation(summary = "删除")
    @PostMapping("/remove")
    @OperateLog(title = "删除微信分账接收方绑定", businessType = OperateLog.BusinessType.DELETE, saveParam = true)
    public Result<Void> remove(@NotNull(message = "主键不可为空") Long id){
        receiverBindService.remove(id);
        return Res.ok();
    }
}
