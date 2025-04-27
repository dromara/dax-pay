package org.dromara.daxpay.channel.alipay.controller.allocation;

import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.channel.alipay.param.allocation.AlipayAllocReceiverParam;
import org.dromara.daxpay.channel.alipay.result.allocation.AlipayAllocReceiverResult;
import org.dromara.daxpay.channel.alipay.service.allocation.AlipayAllocReceiverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 支付宝分账接收方管理
 * @author xxm
 * @since 2025/1/26
 */
@Validated
@Tag(name = "支付宝分账接收方管理")
@RestController
@RequestGroup(groupCode = "AlipayAllocReceiver", moduleCode = "alipay")
@RequestMapping("/alipay/allocation/receiver")
@RequiredArgsConstructor
public class AlipayAllocReceiverController {
    private final AlipayAllocReceiverService receiverService;

    @RequestPath("分账接收方详情")
    @Operation(summary = "分账接收方详情")
    @GetMapping("/findById")
    public Result<AlipayAllocReceiverResult> findById(@NotNull(message = "主键不可为空") Long id) {
        return Res.ok(receiverService.findById(id));
    }

    @RequestPath("新增或修改分账接收方")
    @Operation(summary = "新增或修改分账接收方")
    @PostMapping("/saveOrUpdate")
    @OperateLog(title = "新增或修改支付宝分账接收方", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> saveOrUpdate(@RequestBody @Validated AlipayAllocReceiverParam param) {
        receiverService.saveOrUpdate(param);
        return Res.ok();
    }
}
