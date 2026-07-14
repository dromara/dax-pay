package cn.daxpay.open.platform.notify.controller.wechat;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.system.param.config.notify.PlatformWechatNotifyConfigParam;
import cn.daxpay.open.platform.system.result.config.notify.PlatformWechatNotifyConfigResult;
import cn.daxpay.open.platform.system.service.config.notify.PlatformWechatNotifyConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 微信消息通知配置管理(管理端)
///
/// 仅管理场景模板 Id, 存于系统平台非加密配置 `wechat_notify`.
/// 公众号凭据见三方平台管理.
@PermCode(menuCode = PermCodes.System.WechatNotify.MENU)
@Tag(name = "微信消息通知配置")
@RestController
@RequestMapping("/notify/wechat/config")
@RequiredArgsConstructor
public class WechatConfigController {

    private final PlatformWechatNotifyConfigService wechatNotifyConfigService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "查询配置")
    @GetMapping("/find")
    public Result<PlatformWechatNotifyConfigResult> find() {
        return Res.ok(wechatNotifyConfigService.findConfig());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新配置")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody PlatformWechatNotifyConfigParam param) {
        wechatNotifyConfigService.updateConfig(param);
        return Res.ok();
    }
}
