package cn.bootx.platform.iam.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.iam.core.user.service.UserAssistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 用户操作支撑服务
 *
 * @author xxm
 * @since 2022/6/19
 */
@Validated
@IgnoreAuth(ignore = false, login = true)
@Tag(name = "用户操作支撑服务")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserAssistController {

    private final UserAssistService userAssistService;

    @Operation(summary = "给当前用户发送更改手机号验证码")
    @PostMapping("/sendCurrentPhoneChangeCaptcha")
    public ResResult<Void> sendCurrentPhoneChangeCaptcha() {
        userAssistService.sendCurrentPhoneChangeCaptcha();
        return Res.ok();
    }

    @Operation(summary = "验证当前用户发送更改手机号验证码")
    @GetMapping("/validateCurrentPhoneChangeCaptcha")
    public ResResult<Boolean> validateCurrentPhoneChangeCaptcha(@NotBlank(message = "验证码不可为空") String captcha) {
        return Res.ok(userAssistService.validateCurrentPhoneChangeCaptcha(captcha));
    }

    @Operation(summary = "发送更改/绑定手机号验证码")
    @PostMapping("/sendPhoneChangeCaptcha")
    public ResResult<Void> sendPhoneChangeCaptcha(@NotBlank(message = "手机号不可为空") String phone) {
        userAssistService.sendPhoneChangeCaptcha(phone);
        return Res.ok();
    }

    @Operation(summary = "验证改/绑定手机验证码")
    @GetMapping("/validatePhoneChangeCaptcha")
    public ResResult<Boolean> validatePhoneChangeCaptcha(@NotBlank(message = "手机号不可为空") String phone,
            @NotBlank(message = "验证码不可为空") String captcha) {
        return Res.ok(userAssistService.validatePhoneChangeCaptcha(phone, captcha));
    }

    @IgnoreAuth(ignore = false, login = true)
    @Operation(summary = "给当前用户发送更改邮箱验证码")
    @PostMapping("/sendCurrentEmailChangeCaptcha")
    public ResResult<Void> sendCurrentEmailChangeCaptcha() {
        userAssistService.sendCurrentEmailChangeCaptcha();
        return Res.ok();
    }

    @Operation(summary = "验证当前用户发送更改邮箱验证码")
    @GetMapping("/validateCurrentChangeEmailCaptcha")
    public ResResult<Boolean> validateCurrentChangeEmailCaptcha(@NotBlank(message = "验证码不可为空") String captcha) {
        return Res.ok(userAssistService.validateCurrentChangeEmailCaptcha(captcha));
    }

    @Operation(summary = "发送更改/绑定邮箱验证码")
    @PostMapping("/sendEmailChangeCaptcha")
    public ResResult<Void> sendEmailChangeCaptcha(@NotBlank(message = "邮箱不可为空") @Email String email) {
        userAssistService.sendEmailChangeCaptcha(email);
        return Res.ok();
    }

    @Operation(summary = "验证更改/绑定邮箱验证码")
    @GetMapping("/validateEmailChangeCaptcha")
    public ResResult<Boolean> validateEmailCaptcha(
            @NotBlank(message = "邮箱不可为空") @Email(message = "请输入正确的邮箱") String email,
            @NotBlank(message = "验证码不可为空") String captcha) {
        return Res.ok(userAssistService.validateEmailChangeCaptcha(email, captcha));
    }

    @Operation(summary = "发送找回密码手机验证码")
    @PostMapping("/sendPhoneForgetCaptcha")
    public ResResult<Void> sendPhoneForgetCaptcha(@NotBlank(message = "手机号不可为空") String phone) {
        userAssistService.sendPhoneForgetCaptcha(phone);
        return Res.ok();
    }

    @Operation(summary = "验证找回密码手机验证码")
    @GetMapping("/validatePhoneForgetCaptcha")
    public ResResult<Boolean> validatePhoneForgetCaptcha(@NotBlank(message = "手机号不可为空") String phone,
            @NotBlank(message = "验证码不可为空") String captcha) {
        return Res.ok(userAssistService.validatePhoneForgetCaptcha(phone, captcha));
    }

    @Operation(summary = "发送找回密码邮箱验证码")
    @PostMapping("/sendEmailForgetCaptcha")
    public ResResult<Void> sendEmailForgetCaptcha(@NotBlank(message = "邮箱不可为空") @Email String email) {
        userAssistService.sendEmailForgetCaptcha(email);
        return Res.ok();
    }

    @Operation(summary = "验证找回密码邮箱验证码")
    @GetMapping("/validateEmailForgetCaptcha")
    public ResResult<Boolean> validateEmailForgetCaptcha(
            @NotBlank(message = "邮箱不可为空") @Email(message = "请输入正确的邮箱") String email,
            @NotBlank(message = "验证码不可为空") String captcha) {
        return Res.ok(userAssistService.validateEmailForgetCaptcha(email, captcha));
    }

}
