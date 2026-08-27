package cn.daxpay.open.platform.notify.controller.mail;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.notify.param.mail.MailTestSendParam;
import cn.daxpay.open.platform.notify.param.mail.NotifyMailRecordQuery;
import cn.daxpay.open.platform.notify.result.mail.NotifyMailRecordResult;
import cn.daxpay.open.platform.notify.dao.mail.NotifyMailRecordManager;
import cn.daxpay.open.platform.notify.entity.mail.NotifyMailRecord;
import cn.daxpay.open.platform.notify.service.mail.MailSendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// 邮件通知(管理端)
@PermCode(menuCode = PermCodes.System.MailRecord.MENU)
@Validated
@Tag(name = "邮件通知管理")
@RestController
@RequestMapping("/notify/mail")
@RequiredArgsConstructor
public class NotifyMailRecordController {

    private final NotifyMailRecordManager recordManager;

    private final MailSendService mailSendService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "邮件发送记录分页")
    @GetMapping("/page")
    public Result<PageResult<NotifyMailRecordResult>> page(PageParam pageParam, NotifyMailRecordQuery query) {
        return Res.ok(MpUtil.toPageResult(recordManager.page(pageParam, query)));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "邮件发送记录详情")
    @GetMapping("/get")
    public Result<NotifyMailRecordResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(recordManager.findById(id)
                .map(NotifyMailRecord::toResult)
                .orElseThrow(() -> new DataNotExistException("error.notify.mail.notExist")));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除邮件发送记录")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        recordManager.deleteById(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.RESEND)
    @Operation(summary = "失败邮件重发")
    @PostMapping("/resend")
    public Result<Void> resend(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        mailSendService.resend(id);
        return Res.ok();
    }

    /// 测试发送入口在平台配置页签, 权限挂平台配置菜单而非邮件记录菜单
    @PermCode(menuCode = PermCodes.System.PlatformConfig.MENU, code = PermCodes.Action.TEST)
    @Operation(summary = "发送测试邮件")
    @PostMapping("/test-send")
    public Result<Void> testSend(@RequestBody @Validated MailTestSendParam param) {
        mailSendService.testSend(param);
        return Res.ok();
    }
}
