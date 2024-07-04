package cn.daxpay.multi.admin.controller;

import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.iam.service.permission.PermPathSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author xxm
 * @since 2024/7/4
 */
@Tag(name = "测试服务")
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final PermPathSyncService permPathSyncService;

    @Operation(summary = "测试路径生成")
    @GetMapping("/getRequestPaths")
    public Result<Void> getRequestPaths(){
        permPathSyncService.sync();
        return Res.ok();
    }

}
