package cn.bootx.platform.daxpay.admin.controller.system;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.daxpay.service.core.system.config.service.PlatformConfigService;
import cn.bootx.platform.daxpay.service.dto.system.config.PlatformConfigDto;
import cn.bootx.platform.daxpay.service.param.system.config.PlatformConfigParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 平台配置项
 * @author xxm
 * @since 2024/1/2
 */
@Tag(name = "平台配置项")
@RestController
@RequestMapping("/platform/config")
@RequiredArgsConstructor
public class PlatformConfigController {
    private final PlatformConfigService platformConfigService;

    @Operation(summary = "平台配置项")
    @GetMapping("/getConfig")
    public ResResult<PlatformConfigDto> getConfig(){
        return Res.ok(platformConfigService.getConfig().toDto());
    }

    @Operation(summary = "更新平台配置项")
    @PostMapping("/update")
    public ResResult<Void> update(@RequestBody PlatformConfigParam param){
        platformConfigService.update(param);
        return Res.ok();
    }
}
