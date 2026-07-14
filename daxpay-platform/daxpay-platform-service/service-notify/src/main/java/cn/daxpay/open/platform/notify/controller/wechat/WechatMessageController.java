package cn.daxpay.open.platform.notify.controller.wechat;

import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.capability.wechat.message.result.MessageSendResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.notify.param.wechat.WechatMessageQuery;
import cn.daxpay.open.platform.notify.result.wechat.WechatMessageRecordResult;
import cn.daxpay.open.platform.notify.service.wechat.WechatMessageRecordService;
import cn.daxpay.open.platform.notify.service.wechat.WechatNotifyService;
import cn.daxpay.open.platform.system.service.config.notify.PlatformWechatNotifyConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/// # 微信消息通知记录管理(管理端)
///
/// 与配置页共用菜单 [system:notify:wechat-config], 页内 Tabs 切换.
/// 查询发送记录 / 失败重发 / 测试发送(给当前登录用户发一条, 验证配置与绑定链路).
@PermCode(menuCode = PermCodes.System.WechatNotify.MENU)
@Validated
@Tag(name = "微信消息通知记录")
@RestController
@RequestMapping("/notify/wechat/message")
@RequiredArgsConstructor
public class WechatMessageController {

    private final WechatMessageRecordService messageRecordService;

    private final WechatNotifyService wechatNotifyService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<WechatMessageRecordResult>> page(PageParam pageParam, WechatMessageQuery query) {
        return Res.ok(messageRecordService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "记录详情")
    @GetMapping("/get")
    public Result<WechatMessageRecordResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(messageRecordService.findById(id));
    }

    @PermCode(code = PermCodes.Action.RESEND)
    @Operation(summary = "重发失败消息")
    @PostMapping("/resend")
    public Result<Void> resend(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        messageRecordService.resend(id);
        return Res.ok();
    }

    /// 测试发送(给当前登录用户发一条操作通知, 验证配置 + 绑定链路是否打通)
    @PermCode(code = PermCodes.Action.TEST)
    @Operation(summary = "测试发送(发给当前登录用户)")
    @PostMapping("/test-send")
    public Result<MessageSendResult> testSend() {
        Long userId = SecurityUtil.getUserId();
        // 使用操作通知模板, 固定测试内容
        Map<String, String> data = Map.of(
                "first", "这是一条测试通知",
                "remark", "用于验证公众号模板通知配置是否正确"
        );
        return Res.ok(wechatNotifyService.sendToUser(userId, PlatformWechatNotifyConfigService.SCENE_OPERATE, data, null));
    }
}
