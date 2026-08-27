package cn.daxpay.open.platform.iam.controller.user;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import java.util.List;

import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.iam.auth.service.passkey.PasskeyService;
import cn.daxpay.open.platform.iam.param.passkey.PasskeyDeleteParam;
import cn.daxpay.open.platform.iam.param.passkey.PasskeyRegisterOptionsParam;
import cn.daxpay.open.platform.iam.param.passkey.PasskeyRegisterParam;
import cn.daxpay.open.platform.iam.param.passkey.PasskeyRenameParam;
import cn.daxpay.open.platform.iam.result.passkey.PasskeyRegisterOptionsResult;
import cn.daxpay.open.platform.iam.result.passkey.UserPasskeyResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 用户通行密钥管理
///
/// 提供个人中心通行密钥页的注册/列表/改名/删除接口, 均需登录;
/// 注册与删除须以登录密码二次确认。
///
@IgnoreAuth(login = true)
@Validated
@Tag(name = "用户通行密钥")
@RestController
@RequestMapping("/user/auth/passkey")
@RequiredArgsConstructor
public class UserPasskeyController {

    private final PasskeyService passkeyService;

    @Operation(summary = "获取注册选项(需密码确认)")
    @PostMapping("/register-options")
    public Result<PasskeyRegisterOptionsResult> registerOptions(@RequestBody @Validated PasskeyRegisterOptionsParam param) {
        return Res.ok(passkeyService.registerOptions(param.getPassword()));
    }

    @Operation(summary = "确认注册(验证并绑定凭据)")
    @PostMapping("/register")
    public Result<UserPasskeyResult> register(@RequestBody @Validated PasskeyRegisterParam param) {
        return Res.ok(passkeyService.register(param));
    }

    @Operation(summary = "已绑定的通行密钥列表")
    @GetMapping("/list")
    public Result<List<UserPasskeyResult>> list() {
        return Res.ok(passkeyService.list());
    }

    @Operation(summary = "重命名通行密钥")
    @PostMapping("/rename")
    public Result<Void> rename(@RequestBody @Validated PasskeyRenameParam param) {
        passkeyService.rename(param.getId(), param.getDeviceName());
        return Res.ok();
    }

    @Operation(summary = "删除通行密钥(需密码确认)")
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody @Validated PasskeyDeleteParam param) {
        passkeyService.delete(param.getId(), param.getPassword());
        return Res.ok();
    }
}
