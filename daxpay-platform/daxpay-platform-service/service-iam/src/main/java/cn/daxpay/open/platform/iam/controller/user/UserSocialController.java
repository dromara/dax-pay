package cn.daxpay.open.platform.iam.controller.user;

import java.util.List;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.iam.result.social.SocialBindResult;
import cn.daxpay.open.platform.iam.service.social.IamUserSocialBindStore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// # 用户三方账号绑定管理(管理员)
///
/// 归属用户管理域, 与 [UserAdminController] 同包同 menuCode,
/// 直接注入 [IamUserSocialBindStore] 操作数据层, 与登录/绑定流程([SocialEndpoint])完全分离.
///
@PermCode(menuCode = "iam:user:manager")
@Validated
@Tag(name = "用户三方账号绑定管理")
@RestController
@RequestMapping("/user/admin/social")
@RequiredArgsConstructor
public class UserSocialController {

    private final IamUserSocialBindStore socialBindStore;

    /// 查询指定用户的第三方账号绑定列表
    /// @param userId 目标用户ID
    @PermCode(code = "view", nameCn = "用户查看", nameEn = "User View")
    @Operation(summary = "查询指定用户的第三方账号绑定列表")
    @GetMapping("/bind-list")
    public Result<List<SocialBindResult>> bindList(@NotNull(message = "{validation.field.userId.notNull}") Long userId) {
        return Res.ok(socialBindStore.findBindsByUserId(userId));
    }

    /// 解除指定用户的第三方账号绑定
    /// @param userId 目标用户ID
    /// @param source 平台编码
    @PermCode(code = "view", nameCn = "用户查看", nameEn = "User View")
    @Operation(summary = "解除指定用户的第三方账号绑定")
    @PostMapping("/unbind")
    public Result<Void> unbind(@NotNull(message = "{validation.field.userId.notNull}") Long userId,
                               @NotBlank(message = "{validation.field.source.notBlank}") String source) {
        socialBindStore.removeBind(userId, source);
        return Res.ok();
    }
}
