package cn.daxpay.open.platform.iam.controller.user;

import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.iam.param.user.UserBaseInfoParam;
import cn.daxpay.open.platform.iam.result.user.LoginAfterUserInfoResult;
import cn.daxpay.open.platform.iam.result.user.UserBaseInfoResult;
import cn.daxpay.open.platform.iam.service.user.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 用户认证控制器
///
/// 提供认证流程/登录流程中的接口，需要登录
@Validated
@Tag(name = "用户认证")
@RestController
@RequestMapping("/user/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserInfoService userInfoService;

    @Operation(summary = "修改密码")
    @PostMapping("/update-password")
    public Result<Void> updatePassword(
        @NotBlank(message = "{validation.field.oldPassword.notBlank}") String password,
        @NotBlank(message = "{validation.field.newPassword.notBlank}") String newPassword) {
        userInfoService.updatePassword(password, newPassword);
        return Res.ok();
    }

    @Operation(summary = "查询用户基础信息")
    @GetMapping("/get-user-base-info")
    public Result<UserBaseInfoResult> getUserBaseInfo() {
        return Res.ok(userInfoService.getUserBaseInfo());
    }

    @Operation(summary = "修改用户基础信息")
    @PostMapping("/update-base-info")
    public Result<Void> updateBaseInfo(@RequestBody @Validated UserBaseInfoParam param) {
        userInfoService.updateUserBaseInfo(param);
        return Res.ok();
    }

    @Operation(summary = "登录后获取用户信息")
    @GetMapping("/get-login-after-user-info")
    public Result<LoginAfterUserInfoResult> getLoginAfterUserInfo() {
        return Res.ok(userInfoService.getLoginAfterUserInfo());
    }
}
