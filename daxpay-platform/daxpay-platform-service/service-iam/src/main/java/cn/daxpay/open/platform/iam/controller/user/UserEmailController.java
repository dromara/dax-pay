package cn.daxpay.open.platform.iam.controller.user;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.iam.auth.service.email.UserEmailService;
import cn.daxpay.open.platform.iam.param.user.EmailBindConfirmParam;
import cn.daxpay.open.platform.iam.param.user.EmailBindSendCodeParam;
import cn.daxpay.open.platform.iam.param.user.EmailUnbindParam;
import cn.daxpay.open.platform.iam.param.user.EmailUnbindSendCodeParam;
import cn.daxpay.open.platform.iam.result.user.EmailInfoResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 用户邮箱绑定控制器
///
/// 个人安全设置中的邮箱绑定/换绑/解绑, 需要登录
@Validated
@IgnoreAuth(login = true)
@Tag(name = "用户邮箱绑定")
@RestController
@RequestMapping("/user/auth/email")
@RequiredArgsConstructor
public class UserEmailController {

    private final UserEmailService userEmailService;

    @Operation(summary = "查询邮箱绑定状态")
    @GetMapping("/get-info")
    public Result<EmailInfoResult> getEmailInfo() {
        return Res.ok(userEmailService.getEmailInfo());
    }

    @Operation(summary = "发送邮箱绑定验证码")
    @PostMapping("/send-bind-code")
    public Result<Void> sendBindCode(@RequestBody @Validated EmailBindSendCodeParam param) {
        userEmailService.sendBindCode(param);
        return Res.ok();
    }

    @Operation(summary = "确认邮箱绑定")
    @PostMapping("/bind-confirm")
    public Result<Void> bindConfirm(@RequestBody @Validated EmailBindConfirmParam param) {
        userEmailService.bindConfirm(param);
        return Res.ok();
    }

    @Operation(summary = "发送邮箱解绑验证码")
    @PostMapping("/send-unbind-code")
    public Result<Void> sendUnbindCode(@RequestBody @Validated EmailUnbindSendCodeParam param) {
        userEmailService.sendUnbindCode(param);
        return Res.ok();
    }

    @Operation(summary = "解绑邮箱")
    @PostMapping("/unbind")
    public Result<Void> unbind(@RequestBody @Validated EmailUnbindParam param) {
        userEmailService.unbind(param);
        return Res.ok();
    }
}
