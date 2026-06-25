package cn.daxpay.open.platform.iam.controller.user;

import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.iam.param.twofactor.TwoFactorCodeParam;
import cn.daxpay.open.platform.iam.result.twofactor.BackupCodeResult;
import cn.daxpay.open.platform.iam.result.twofactor.TwoFactorSetupResult;
import cn.daxpay.open.platform.iam.result.twofactor.TwoFactorStatusResult;
import cn.daxpay.open.platform.iam.service.twofactor.UserTwoFactorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 用户双因素认证管理
///
/// 提供个人中心安全设置页的双因素认证绑定/解绑/备用码管理接口, 均需登录。
///
@Validated
@Tag(name = "用户双因素认证")
@RestController
@RequestMapping("/user/auth/two-factor")
@RequiredArgsConstructor
public class UserTwoFactorController {

    private final UserTwoFactorService userTwoFactorService;

    @Operation(summary = "查询双因素认证状态")
    @GetMapping("/status")
    public Result<TwoFactorStatusResult> status() {
        return Res.ok(userTwoFactorService.getStatus());
    }

    @Operation(summary = "初始化绑定(生成密钥与二维码URI)")
    @PostMapping("/setup")
    public Result<TwoFactorSetupResult> setup() {
        return Res.ok(userTwoFactorService.setup());
    }

    @Operation(summary = "确认绑定(校验动态码并启用)")
    @PostMapping("/confirm")
    public Result<BackupCodeResult> confirm(@RequestBody @Validated TwoFactorCodeParam param) {
        return Res.ok(userTwoFactorService.confirm(param.getSecret(), param.getCode()));
    }

    @Operation(summary = "关闭双因素认证")
    @PostMapping("/disable")
    public Result<Void> disable(@RequestBody @Validated TwoFactorCodeParam param) {
        userTwoFactorService.disable(param.getCode());
        return Res.ok();
    }

    @Operation(summary = "重新生成备用验证码")
    @PostMapping("/regenerate-backup-codes")
    public Result<BackupCodeResult> regenerateBackupCodes(@RequestBody @Validated TwoFactorCodeParam param) {
        return Res.ok(userTwoFactorService.regenerateBackupCodes(param.getCode()));
    }
}
