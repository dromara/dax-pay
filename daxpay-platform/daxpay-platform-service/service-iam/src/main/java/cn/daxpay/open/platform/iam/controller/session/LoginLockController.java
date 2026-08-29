package cn.daxpay.open.platform.iam.controller.session;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.iam.param.session.LoginLockQuery;
import cn.daxpay.open.platform.iam.result.session.LoginLockPageResult;
import cn.daxpay.open.platform.iam.service.session.LoginLockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 登录锁定监控
///
/// 系统监控"锁定用户"页面: 展示登录重试锁定状态(登录失败与敏感操作验证失败共用计数器),
/// 支持管理员手动解锁。真攻击场景的正确动作是封禁(ban)而非解锁, 解锁仅用于协助被误锁的合法用户。
@PermCode(menuCode = PermCodes.Iam.Lock.MENU)
@Validated
@Tag(name = "登录锁定监控")
@RestController
@RequestMapping("/login-lock")
@RequiredArgsConstructor
public class LoginLockController {

    private final LoginLockService loginLockService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "锁定用户分页")
    @GetMapping("/page")
    public Result<LoginLockPageResult> page(PageParam pageParam, LoginLockQuery query) {
        return Res.ok(loginLockService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.UNLOCK)
    @Operation(summary = "解锁用户登录锁定")
    @PostMapping("/unlock")
    public Result<Void> unlock(Long userId) {
        loginLockService.unlock(userId);
        return Res.ok();
    }
}
