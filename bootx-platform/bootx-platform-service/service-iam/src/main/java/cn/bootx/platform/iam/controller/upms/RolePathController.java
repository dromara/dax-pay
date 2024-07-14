package cn.bootx.platform.iam.controller.upms;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import cn.bootx.platform.iam.param.permission.PermPathAssignParam;
import cn.bootx.platform.iam.result.permission.SimplePermPathResult;
import cn.bootx.platform.iam.service.upms.RolePathService;
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
@RequestGroup(groupCode = "upms", moduleCode = "iam")
public class RolePathController {

    private final RolePathService rolePathService;

    @RequestPath("保存角色请求权限关联关系")
    @Operation(summary = "保存角色请求权限关联关系")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody PermPathAssignParam param) {
        ValidationUtil.validateParam(param);
        rolePathService.saveAssign(param);
        return Res.ok();
    }

    @RequestPath("指定角色下的请求权限树(分配时用)")
    @Operation(summary = "指定角色下的请求权限树(分配时用)")
    @GetMapping("/treeByRole")
    public Result<List<SimplePermPathResult>> treeByRoleAndClient(Long roleId, String clientCode) {
        return Res.ok(rolePathService.treeByRoleAssign(roleId,clientCode));
    }

    @RequestPath("查询当前角色已经选择的请求路径")
    @Operation(summary = "查询当前角色已经选择的请求路径")
    @GetMapping("/findIdsByRole")
    public Result<List<Long>> findIdsByRole(Long roleId, String clientCode) {
        return Res.ok(rolePathService.findIdsByRole(roleId, clientCode));
    }

}
