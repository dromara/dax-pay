package cn.daxpay.multi.merchant.controller.merchant;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import cn.bootx.platform.iam.param.upms.UserRoleParam;
import cn.bootx.platform.iam.param.user.UserInfoParam;
import cn.bootx.platform.iam.param.user.UserInfoQuery;
import cn.bootx.platform.iam.result.user.UserWholeInfoResult;
import cn.daxpay.multi.merchant.service.merchant.MerchantUserPermService;
import cn.daxpay.multi.merchant.service.merchant.MerchantUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 商户用户
 * @author xxm
 * @since 2024/8/23
 */
@RequestGroup(groupCode = "merchant", moduleCode = "PayConfig")
@Tag(name = "商户用户管理")
@RestController
@RequestMapping("/merchant/user")
@RequiredArgsConstructor
public class MerchantUserController {

    private final MerchantUserService merchantUserService;
    private final MerchantUserPermService merchantUserPermService;

    @Operation(summary = "用户列表")
    @GetMapping("/page")
    public Result<PageResult<UserWholeInfoResult>> page(PageParam pageParam, UserInfoQuery query) {
        return Res.ok(merchantUserService.page(pageParam, query));
    }

    @RequestPath("添加用户")
    @Operation(summary = "添加用户")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) UserInfoParam userInfoParam) {
        merchantUserService.add(userInfoParam);
        return Res.ok();
    }

    @RequestPath("修改用户")
    @Operation(summary = "修改用户")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) UserInfoParam userInfoParam) {
        merchantUserService.update(userInfoParam);
        return Res.ok();
    }

   @Operation(summary = "分配角色")
   @PostMapping("/assignRole")
    public Result<Void> assignRole(@Validated @RequestBody UserRoleParam param) {
       merchantUserPermService.assignRole(param.getUserId(), param.getRoleIds());
        return Res.ok();
    }
}
