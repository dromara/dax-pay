package cn.bootx.platform.iam.controller.permission;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.annotation.InternalPath;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
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
import org.springframework.web.bind.annotation.*;

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
@RequestGroup(groupCode = "perm", groupName = "权限管理", moduleCode = "iam")
public class PermCodeController {

    private final PermCodeService permCodeService;

    private final UserRolePremService userRoleService;

    @RequestPath("权限码详情")
    @Operation(summary = "权限码详情")
    @GetMapping("/findById")
    public Result<PermCodeResult> findById(Long id) {
        return Res.ok(permCodeService.findById(id));
    }

    @InternalPath
    @Operation(summary = "添加权限码")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody PermCodeParam param) {
        permCodeService.add(param);
        return Res.ok();
    }

    @InternalPath
    @Operation(summary = "更新权限码")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody PermCodeParam param) {
        permCodeService.update(param);
        return Res.ok();
    }

    @InternalPath
    @Operation(summary = "删除权限码")
    @PostMapping("/delete")
    public Result<Void> delete(Long id) {
        permCodeService.delete(id);
        return Res.ok();
    }

    @InternalPath
    @Operation(summary = "权限码树")
    @GetMapping("/tree")
    public Result<List<PermCodeResult>> tree() {
        UserDetail user = SecurityUtil.getUser();
        if (user.isAdmin()){
            return Res.ok(permCodeService.tree());
        }
        return Res.ok(userRoleService.codeTreeByUser(user.getId()));
    }


    @RequestPath("权限码目录树")
    @Operation(summary = "权限码目录树")
    @GetMapping("/catalogTree")
    public Result<List<PermCodeResult>> catalogTree() {
        return Res.ok(permCodeService.catalogTree());
    }

    @IgnoreAuth
    @Operation(summary = "根据用户获取权限码")
    @GetMapping("/findCodesByUser")
    public Result<List<String>> findCodesByUser() {
        UserDetail user = SecurityUtil.getUser();
        if (user.isAdmin()){
            return Res.ok(permCodeService.findAllCode());
        }
        return Res.ok(userRoleService.findAllCodesByUser(user.getId()));
    }

    @RequestPath("编码是否被使用")
    @Operation(summary = "编码是否被使用")
    @GetMapping("/existsByCode")
    public Result<Boolean> existsByPermCode(String code) {
        return Res.ok(permCodeService.existsByCode(code));
    }

    @RequestPath("编码是否被使用(不包含自己)")
    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public Result<Boolean> existsByPermCode(String code, Long id) {
        return Res.ok(permCodeService.existsByPermCode(code, id));
    }
}
