package cn.bootx.platform.daxpay.admin.controller.system;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.daxpay.service.core.system.service.WechatNoticeConfigService;
import cn.bootx.platform.daxpay.service.dto.system.WechatNoticeConfigDto;
import cn.bootx.platform.daxpay.service.param.system.WechatNoticeConfigParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 微信消息通知配置
 * @author xxm
 * @since 2024/1/2
 */
@Tag(name = "微信消息通知配置")
@RestController
@RequestMapping("/wx/notice")
@RequiredArgsConstructor
public class WechatNoticeConfigController {
    private final WechatNoticeConfigService configService;

    @Operation(summary = "获取微信消息通知配置")
    @GetMapping("/getConfig")
    public ResResult<WechatNoticeConfigDto> getConfig() {
        return Res.ok(configService.getConfig().toDto());
    }

    @Operation(summary = "更新微信消息通知配置")
    @PostMapping("/update")
    public ResResult<Void> update(@RequestBody WechatNoticeConfigParam param) {
        configService.update(param);
        return Res.ok();
    }
}
