package org.dromara.daxpay.service.controller.unipay;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import org.dromara.daxpay.core.result.DaxResult;
import org.dromara.daxpay.core.util.DaxRes;
import org.dromara.daxpay.service.common.anno.PaymentVerify;
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
@PaymentVerify
@IgnoreAuth
@Tag(name = "分账控制器")
@RestController
@RequestMapping("/unipay/alloc")
@RequiredArgsConstructor
public class UniAllocationController {

    @Operation(summary = "发起分账接口")
    @PostMapping("/start")
    public DaxResult<Void> start(){
        return DaxRes.ok();
    }

    @Operation(summary = "分账完结接口")
    @PostMapping("/finish")
    public DaxResult<Void> finish(){
        return DaxRes.ok();
    }

    @Operation(summary = "分账接收方添加接口")
    @PostMapping("/receiver/add")
    public DaxResult<Void> receiverAdd(){
        return DaxRes.ok();
    }

    @Operation(summary = "分账接收方删除接口")
    @PostMapping("/receiver/remove")
    public DaxResult<Void> receiverRemove(){
        return DaxRes.ok();
    }

}
