package cn.daxpay.open.platform.iam.controller.user;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.iam.service.user.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 用户校验控制器
///
/// 提供注册/修改时的校验性接口，不需要登录
@Validated
@IgnoreAuth
@Tag(name = "用户校验")
@RestController
@RequestMapping("/user/check")
@RequiredArgsConstructor
public class UserCheckController {

    private final UserQueryService userQueryService;

    @Operation(summary = "账号是否被使用")
    @GetMapping("/exists-account")
    public Result<Boolean> existsAccount(@NotBlank(message = "{validation.field.account.notBlank}") String account) {
        return Res.ok(userQueryService.existsAccount(account));
    }

    @Operation(summary = "账号是否被使用(不包含自己)")
    @GetMapping("/exists-account-not-id")
    public Result<Boolean> existsAccount(
        @NotBlank(message = "{validation.field.account.notBlank}") @Parameter(description = "账号") String account,
        @NotNull(message = "{validation.field.userId.notNull}") @Parameter(description = "用户ID") Long id) {
        return Res.ok(userQueryService.existsAccount(account, id));
    }

    @Operation(summary = "按终端校验账号是否被使用（终端维度唯一性）")
    @GetMapping("/exists-account-by-client")
    public Result<Boolean> existsAccountByClient(
        @NotBlank(message = "{validation.field.account.notBlank}") @Parameter(description = "账号") String account,
        @NotBlank(message = "{validation.field.clientCode.notBlank}") @Parameter(description = "身份域编码") String clientCode) {
        return Res.ok(userQueryService.existsAccountByClientCode(clientCode, account));
    }

    @Operation(summary = "按终端校验账号是否被使用，排除指定用户ID（终端维度编辑防重）")
    @GetMapping("/exists-account-by-client-not-id")
    public Result<Boolean> existsAccountByClient(
        @NotBlank(message = "{validation.field.account.notBlank}") @Parameter(description = "账号") String account,
        @NotBlank(message = "{validation.field.clientCode.notBlank}") @Parameter(description = "身份域编码") String clientCode,
        @NotNull(message = "{validation.field.userId.notNull}") @Parameter(description = "用户ID") Long id) {
        return Res.ok(userQueryService.existsAccountByClientCode(clientCode, account, id));
    }

    // 手机号校验端点已随手机号功能冻结一并移除(无短信验证体系, 待接入后恢复)

    @Operation(summary = "邮箱是否被使用")
    @GetMapping("/exists-email")
    public Result<Boolean> existsEmail(@NotBlank(message = "{validation.field.email.notBlank}") @Parameter(description = "邮箱") String email) {
        return Res.ok(userQueryService.existsEmail(email));
    }

    @Operation(summary = "邮箱是否被使用(不包含自己)")
    @GetMapping("/exists-email-not-id")
    public Result<Boolean> existsEmail(
        @NotBlank(message = "{validation.field.email.notBlank}") @Parameter(description = "邮箱") String email,
        @NotNull(message = "{validation.field.userId.notNull}") @Parameter(description = "用户ID") Long id) {
        return Res.ok(userQueryService.existsEmail(email, id));
    }

    @Operation(summary = "按终端校验邮箱是否被使用（终端维度唯一性）")
    @GetMapping("/exists-email-by-client")
    public Result<Boolean> existsEmailByClient(
        @Parameter(description = "邮箱") String email,
        @NotBlank(message = "{validation.field.clientCode.notBlank}") @Parameter(description = "身份域编码") String clientCode) {
        return Res.ok(userQueryService.existsEmailByClientCode(clientCode, email));
    }

    @Operation(summary = "按终端校验邮箱是否被使用，排除指定用户ID（终端维度编辑防重）")
    @GetMapping("/exists-email-by-client-not-id")
    public Result<Boolean> existsEmailByClient(
        @Parameter(description = "邮箱") String email,
        @NotBlank(message = "{validation.field.clientCode.notBlank}") @Parameter(description = "身份域编码") String clientCode,
        @NotNull(message = "{validation.field.userId.notNull}") @Parameter(description = "用户ID") Long id) {
        return Res.ok(userQueryService.existsEmailByClientCode(clientCode, email, id));
    }
}
