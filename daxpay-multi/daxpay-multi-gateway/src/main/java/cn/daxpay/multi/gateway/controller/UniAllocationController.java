package cn.daxpay.multi.gateway.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.daxpay.multi.service.common.anno.PaymentVerify;
import cn.daxpay.multi.core.result.DaxResult;
import cn.daxpay.multi.core.util.DaxRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分账控制器
 * @author xxm
 * @since 2024/5/17
 */
@IgnoreAuth
@Tag(name = "分账控制器")
@RestController
@RequestMapping("/unipay/alloc")
@RequiredArgsConstructor
public class UniAllocationController {

    @PaymentVerify
    @Operation(summary = "发起分账接口")
    @PostMapping("/start")
    public DaxResult<Void> start(){
        return DaxRes.ok();
    }

    @PaymentVerify
    @Operation(summary = "分账完结接口")
    @PostMapping("/finish")
    public DaxResult<Void> finish(){
        return DaxRes.ok();
    }

    @PaymentVerify
    @Operation(summary = "分账接收方添加接口")
    @PostMapping("/receiver/add")
    public DaxResult<Void> receiverAdd(){
        return DaxRes.ok();
    }

    @PaymentVerify
    @Operation(summary = "分账接收方删除接口")
    @PostMapping("/receiver/remove")
    public DaxResult<Void> receiverRemove(){
        return DaxRes.ok();
    }

}
