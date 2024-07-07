package cn.bootx.platform.iam.controller;

import cn.bootx.platform.core.entity.UserDetail;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.iam.param.permission.PermCodeParam;
import cn.bootx.platform.iam.result.permission.PermCodeResult;
import cn.bootx.platform.iam.service.permission.PermCodeService;
import cn.bootx.platform.iam.service.upms.UserRolePremService;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 权限码管理
 * @author xxm
 * @since 2024/7/7
 */
@Tag(name = "权限码管理")
@RestController
@RequestMapping("/perm/code")
@RequiredArgsConstructor
public class PermCodeController {

    private final PermCodeService permCodeService;

    private final UserRolePremService userRoleService;

    @Operation(summary = "添加")
    @PostMapping("/add")
    public Result<Void> add(PermCodeParam param) {
        permCodeService.add(param);
        return Res.ok();
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public Result<Void> update(PermCodeParam param) {
        permCodeService.update(param);
        return Res.ok();
    }

    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(Long id) {
        permCodeService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "权限码树")
    @GetMapping("/tree")
    public Result<List<PermCodeResult>> tree() {
        UserDetail user = SecurityUtil.getUser();
        if (user.isAdmin()){
            return Res.ok(permCodeService.tree());
        }
        return Res.ok(userRoleService.codeTreeByUser(user.getId()));
    }

    @Operation(summary = "根据用户获取权限码")
    @GetMapping("/findPermCodesByUser")
    public Result<List<String>> findPermCodesByUser() {
        return Res.ok(userRoleService.findPermCodesByUser(SecurityUtil.getUserId()));
    }

    @Operation(summary = "编码是否被使用")
    @GetMapping("/existsByPermCode")
    public Result<Boolean> existsByPermCode(String permCode) {
        return Res.ok(permCodeService.existsByCode(permCode));
    }

    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByPermCodeNotId")
    public Result<Boolean> existsByPermCode(String permCode, Long id) {
        return Res.ok(permCodeService.existsByPermCode(permCode, id));
    }
}
