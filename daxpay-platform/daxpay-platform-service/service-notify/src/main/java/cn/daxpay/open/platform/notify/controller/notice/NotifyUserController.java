package cn.daxpay.open.platform.notify.controller.notice;

import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.notify.param.notice.NotifyUserNoticeQuery;
import cn.daxpay.open.platform.notify.result.notice.NotifyNoticeBriefResult;
import cn.daxpay.open.platform.notify.result.notice.NotifyUnreadCountResult;
import cn.daxpay.open.platform.notify.service.notice.NotifySseService;
import cn.daxpay.open.platform.notify.service.notice.NotifyUserNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/// 站内通知(用户端, 登录即可访问)
@IgnoreAuth(login = true)
@Validated
@Tag(name = "站内通知")
@RestController
@RequestMapping("/notify/user")
@RequiredArgsConstructor
public class NotifyUserController {

    private final NotifyUserNoticeService userNoticeService;

    private final NotifySseService sseService;

    @Operation(summary = "未读数")
    @GetMapping("/unread-count")
    public Result<NotifyUnreadCountResult> unreadCount() {
        return Res.ok(userNoticeService.unreadCount());
    }

    @Operation(summary = "铃铛通知列表")
    @GetMapping("/page")
    public Result<List<NotifyNoticeBriefResult>> page(NotifyUserNoticeQuery query) {
        return Res.ok(userNoticeService.list(query));
    }

    /// 查看单条详情(公告校验可见性, 个人消息校验归属)
    ///
    /// 与列表数据隔离: 前端点击查看时独立请求, 保证数据新鲜且不依赖列表上下文.
    @Operation(summary = "查看详情")
    @GetMapping("/detail")
    public Result<NotifyNoticeBriefResult> detail(@NotBlank(message = "{validation.field.type.notBlank}") String type,
                                                  @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(userNoticeService.detail(type, id));
    }

    @Operation(summary = "标记单条已读")
    @PostMapping("/read")
    public Result<Void> read(@NotBlank(message = "{validation.field.type.notBlank}") String type,
                             @NotNull(message = "{validation.field.id.notNull}") Long id) {
        userNoticeService.markRead(type, id);
        return Res.ok();
    }

    @Operation(summary = "全部已读(清空)")
    @PostMapping("/read-all")
    public Result<Void> readAll() {
        userNoticeService.readAll();
        return Res.ok();
    }

    @Operation(summary = "忽略(隐藏)")
    @PostMapping("/ignore")
    public Result<Void> ignore(@NotBlank(message = "{validation.field.type.notBlank}") String type,
                               @NotNull(message = "{validation.field.id.notNull}") Long id) {
        userNoticeService.ignore(type, id);
        return Res.ok();
    }

    /// 建立实时推送连接(Server-Sent Events)
    ///
    /// 浏览器 EventSource 无法自定义请求头, 依赖登录时写入的 `Accesstoken` 同源 Cookie
    /// (withCredentials) 识别会话; 收到推送时前端刷新未读数与铃铛列表.
    @Operation(summary = "建立实时推送连接(SSE)")
    @GetMapping(value = "/sse/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sseConnect() {
        Long userId = SecurityUtil.getUserId();
        return sseService.connect(userId);
    }
}
