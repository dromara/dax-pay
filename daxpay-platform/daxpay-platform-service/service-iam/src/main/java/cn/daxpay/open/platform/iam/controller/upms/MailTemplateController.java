package cn.daxpay.open.platform.iam.controller.upms;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.iam.auth.service.email.MailTemplateAdminService;
import cn.daxpay.open.platform.iam.param.mail.MailTemplatePreviewParam;
import cn.daxpay.open.platform.iam.param.mail.MailTemplateSendTestParam;
import cn.daxpay.open.platform.iam.param.mail.MailTemplateUpsertParam;
import cn.daxpay.open.platform.iam.result.mail.MailPreviewResult;
import cn.daxpay.open.platform.iam.result.mail.MailTemplateResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// 邮件模板管理(运营端)
@PermCode(menuCode = PermCodes.System.MailTemplate.MENU)
@Validated
@Tag(name = "邮件模板管理")
@RestController
@RequestMapping("/notify/mail-template")
@RequiredArgsConstructor
public class MailTemplateController {

    private final MailTemplateAdminService mailTemplateAdminService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "邮件模板列表")
    @GetMapping("/list")
    public Result<List<MailTemplateResult>> list() {
        return Res.ok(mailTemplateAdminService.list());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存邮件模板槽位")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated MailTemplateUpsertParam param) {
        mailTemplateAdminService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "恢复邮件模板默认文案")
    @PostMapping("/reset")
    public Result<Void> reset(@NotBlank(message = "{validation.field.mailTemplateCode.notBlank}") String templateCode,
            @NotBlank(message = "{validation.field.mailLanguage.notBlank}") String language) {
        mailTemplateAdminService.reset(templateCode, language);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "邮件模板预览渲染")
    @PostMapping("/preview")
    public Result<MailPreviewResult> preview(@RequestBody @Validated MailTemplatePreviewParam param) {
        return Res.ok(mailTemplateAdminService.preview(param));
    }

    @PermCode(code = PermCodes.Action.TEST)
    @Operation(summary = "发送模板预览邮件")
    @PostMapping("/send-test")
    public Result<Void> sendTest(@RequestBody @Validated MailTemplateSendTestParam param) {
        mailTemplateAdminService.sendTest(param);
        return Res.ok();
    }
}
