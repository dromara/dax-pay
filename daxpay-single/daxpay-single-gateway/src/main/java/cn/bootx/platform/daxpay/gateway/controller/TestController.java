package cn.bootx.platform.daxpay.gateway.controller;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.daxpay.service.core.timeout.task.PayExpiredTimeTask;
import cn.bootx.platform.daxpay.service.core.timeout.task.PayWaitOrderSyncTask;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 * @author xxm
 * @since 2024/1/5
 */
@Tag(name = "测试")
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final PayExpiredTimeTask expiredTimeTask;;
    private final PayWaitOrderSyncTask waitOrderSyncTask;

    @Operation(summary = "同步")
    @GetMapping("/sync")
    public ResResult<Void> sync(){
        waitOrderSyncTask.task();
        return Res.ok();
    }
    @Operation(summary = "超时")
    @GetMapping("/expired")
    public ResResult<Void> expired(){
        expiredTimeTask.task();
        return Res.ok();
    }

}
