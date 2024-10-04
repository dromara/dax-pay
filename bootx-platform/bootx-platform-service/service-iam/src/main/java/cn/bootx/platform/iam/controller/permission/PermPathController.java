package cn.bootx.platform.iam.controller.permission;

import cn.bootx.platform.core.annotation.InternalPath;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.entity.UserDetail;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.iam.result.permission.PermPathResult;
import cn.bootx.platform.iam.service.permission.PermPathService;
import cn.bootx.platform.iam.service.permission.PermPathSyncService;
import cn.bootx.platform.iam.service.upms.UserRolePremService;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xxm
 * @since 2020/5/11 9:36
 */
@Validated
@Tag(name = "请求权限管理")
@RestController
@RequestMapping("/perm/path")
@RequiredArgsConstructor
@RequestGroup(groupCode = "perm", moduleCode = "iam")
public class PermPathController {

    private final PermPathService pathService;

    private final UserRolePremService userRoleService;

    private final PermPathSyncService permPathSyncService;

    @RequestPath("获取请求权限详情")
    @Operation(summary = "获取详情")
    @GetMapping("/findById")
    public Result<PermPathResult> findById(Long id) {
        return Res.ok(pathService.findById(id));
    }

    @RequestPath("请求权限树")
    @Operation(summary = "请求权限树")
    @GetMapping("/tree")
    public Result<List<PermPathResult>> tree(String clientCode) {
        UserDetail user = SecurityUtil.getUser();
        if (user.isAdmin()){
            return Res.ok(pathService.tree(clientCode));
        }
        return Res.ok(userRoleService.pathTreeByUser(user.getId(),clientCode));
    }

    @InternalPath
    @Operation(summary = "根据系统配置同步请求权限数据")
    @PostMapping("/sync")
    public Result<Void> sync() {
        permPathSyncService.sync();
        return Res.ok();
    }
}
