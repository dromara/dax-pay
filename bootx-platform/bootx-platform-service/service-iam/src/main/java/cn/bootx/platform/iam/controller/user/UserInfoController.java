package cn.bootx.platform.iam.controller.user;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.iam.param.user.UserBaseInfoParam;
import cn.bootx.platform.iam.result.user.LoginAfterUserInfoResult;
import cn.bootx.platform.iam.result.user.UserBaseInfoResult;
import cn.bootx.platform.iam.service.service.UserInfoService;
import cn.bootx.platform.iam.service.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author xxm
 * @since 2020/4/25 20:02
 */
@Validated
@IgnoreAuth
@Tag(name = "用户管理")
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;

    private final UserQueryService userQueryService;

    @Operation(summary = "账号是否被使用")
    @GetMapping("/existsAccount")
    public Result<Boolean> existsAccount(String account) {
        return Res.ok(userQueryService.existsAccount(account));
    }

    @Operation(summary = "账号是否被使用(不包含自己)")
    @GetMapping("/existsAccountNotId")
    public Result<Boolean> existsAccount(String account, Long id) {
        return Res.ok(userQueryService.existsAccount(account, id));
    }

    @Operation(summary = "手机号是否被使用")
    @GetMapping("/existsPhone")
    public Result<Boolean> existsPhone(String phone) {
        return Res.ok(userQueryService.existsPhone(phone));
    }

    @Operation(summary = "手机号是否被使用(不包含自己)")
    @GetMapping("/existsPhoneNotId")
    public Result<Boolean> existsPhone(String phone, Long id) {
        return Res.ok(userQueryService.existsPhone(phone, id));
    }

    @Operation(summary = "邮箱是否被使用")
    @GetMapping("/existsEmail")
    public Result<Boolean> existsEmail(String email) {
        return Res.ok(userQueryService.existsEmail(email));
    }

    @Operation(summary = "邮箱是否被使用(不包含自己)")
    @GetMapping("/existsEmailNotId")
    public Result<Boolean> existsEmail(String email, Long id) {
        return Res.ok(userQueryService.existsEmail(email, id));
    }


    @Operation(summary = "修改密码")
    @PostMapping("/updatePassword")
    public Result<Void> updatePassword(@NotBlank(message = "旧密码不能为空") String password, @NotBlank(message = "新密码不能为空") String newPassword) {
        userInfoService.updatePassword(password, newPassword);
        return Res.ok();
    }

    @Operation(summary = "查询用户基础信息")
    @GetMapping("/getUserBaseInfo")
    public Result<UserBaseInfoResult> getUserBaseInfo() {
        return Res.ok(userInfoService.getUserBaseInfo());
    }

    @Operation(summary = "修改用户基础信息")
    @PostMapping("/updateBaseInfo")
    public Result<Void> updateBaseInfo(@RequestBody @Validated UserBaseInfoParam param) {
        userInfoService.updateUserBaseInfo(param);
        return Res.ok();
    }

    @Operation(summary = "登录后获取用户信息")
    @GetMapping("/getLoginAfterUserInfo")
    public Result<LoginAfterUserInfoResult> getLoginAfterUserInfo() {
        return Res.ok(userInfoService.getLoginAfterUserInfo());
    }

}
