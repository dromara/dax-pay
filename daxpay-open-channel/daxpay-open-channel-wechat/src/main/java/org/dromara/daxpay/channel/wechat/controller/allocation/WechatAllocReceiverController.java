package org.dromara.daxpay.channel.wechat.controller.allocation;

import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.channel.wechat.param.allocation.WechatAllocReceiverParam;
import org.dromara.daxpay.channel.wechat.result.allocation.WechatAllocReceiverResult;
import org.dromara.daxpay.channel.wechat.service.allocation.WechatAllocReceiverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 微信分账接收方
 * @author xxm
 * @since 2025/1/26
 */
@Validated
@Tag(name = "微信分账接收方管理")
@RestController
@RequestGroup(groupCode = "WechatAllocReceiver", moduleCode = "wechatPay")
@RequestMapping("/wechat/allocation/receiver")
@RequiredArgsConstructor
public class WechatAllocReceiverController {
    private final WechatAllocReceiverService receiverService;

    @RequestPath("分账接收方详情")
    @Operation(summary = "分账接收方详情")
    @GetMapping("/findById")
    public Result<WechatAllocReceiverResult> findById(@NotNull(message = "主键不可为空") Long id) {
        return Res.ok(receiverService.findById(id));
    }

    @RequestPath("添加或更新分账接收方")
    @Operation(summary = "添加或更新分账接收方")
    @PostMapping("/saveOrUpdate")
    @OperateLog(title = "添加或更新微信分账接收方", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> saveOrUpdate(@RequestBody @Validated WechatAllocReceiverParam param) {
        receiverService.saveOrUpdate(param);
        return Res.ok();
    }

}
