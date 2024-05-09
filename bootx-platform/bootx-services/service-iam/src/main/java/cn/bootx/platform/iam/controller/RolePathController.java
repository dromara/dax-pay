package cn.bootx.platform.iam.controller;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.bootx.platform.iam.core.upms.service.RolePathService;
import cn.bootx.platform.iam.dto.permission.PermPathDto;
import cn.bootx.platform.iam.param.upms.RolePermissionParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xxm
 * @since 2021/6/9
 */
@Tag(name = "角色请求权限消息关系")
@RestController
@RequestMapping("/role/path")
@RequiredArgsConstructor
public class RolePathController {

    private final RolePathService rolePathService;

    @Operation(summary = "保存角色请求权限关联关系")
    @PostMapping("/save")
    public ResResult<Void> save(@RequestBody RolePermissionParam param) {
        ValidationUtil.validateParam(param);
        rolePathService.addRolePath(param.getRoleId(), param.getPermissionIds(), param.isUpdateChildren());
        return Res.ok();
    }

    @Operation(summary = "获取当前用户角色下可见的请求权限列表(分配时用)")
    @GetMapping("/findPathsByRole")
    public ResResult<List<PermPathDto>> findPathsByRole(Long roleId) {
        return Res.ok(rolePathService.findPathsByRole(roleId));
    }

    @Operation(summary = "根据角色id获取关联权限id")
    @GetMapping("/findIdsByRole")
    public ResResult<List<Long>> findIdsByRole(Long roleId) {
        return Res.ok(rolePathService.findIdsByRole(roleId));
    }

}
