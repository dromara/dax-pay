package cn.bootx.platform.iam.controller;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.bootx.platform.iam.core.upms.service.UserDataRoleService;
import cn.bootx.platform.iam.dto.scope.DataRoleDto;
import cn.bootx.platform.iam.param.upms.UserDataRoleBatchParam;
import cn.bootx.platform.iam.param.upms.UserDataRoleParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author xxm
 * @since 2022/1/2
 */
@Tag(name = "用户数据角色配置")
@RestController
@RequestMapping("/user/data/role")
@RequiredArgsConstructor
public class UserDataRoleController {

    private final UserDataRoleService dataScopeService;

    @Operation(summary = "给用户分配数据角色")
    @PostMapping("/saveAssign")
    public ResResult<Void> saveAssign(@RequestBody UserDataRoleParam param) {
        ValidationUtil.validateParam(param);
        dataScopeService.saveAssign(param.getUserId(), param.getDataRoleId());
        return Res.ok();
    }

    @Operation(summary = "给用户分配权限(批量)")
    @PostMapping("/saveAssignBatch")
    public ResResult<Void> saveAssignBatch(@RequestBody UserDataRoleBatchParam param) {
        dataScopeService.saveAssignBatch(param.getUserIds(), param.getDataRoleId());
        return Res.ok();
    }

    @Operation(summary = "根据用户ID获取到数据角色列表")
    @GetMapping(value = "/findDataRoleByUser")
    public ResResult<DataRoleDto> findDataRoleByUser(Long id) {
        return Res.ok(dataScopeService.findDataRoleByUser(id));
    }

    @Operation(summary = "根据用户ID获取到数据角色Id")
    @GetMapping(value = "/findDataRoleIdByUser")
    public ResResult<Long> findDataRoleIdByUser(Long id) {
        return Res.ok(dataScopeService.findDataRoleIdByUser(id));
    }

}
