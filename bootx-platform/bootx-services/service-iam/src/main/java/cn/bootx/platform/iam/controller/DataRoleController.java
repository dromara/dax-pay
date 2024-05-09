package cn.bootx.platform.iam.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.iam.core.scope.service.DataRoleService;
import cn.bootx.platform.iam.core.scope.service.DataRoleUserService;
import cn.bootx.platform.iam.dto.scope.DataRoleDto;
import cn.bootx.platform.iam.dto.scope.DataRoleUserDto;
import cn.bootx.platform.iam.param.scope.DataRoleParam;
import cn.bootx.platform.iam.param.scope.DataRoleDeptParam;
import cn.bootx.platform.iam.param.scope.DataRoleUserParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xxm
 * @since 2021/12/23
 */
@Tag(name = "数据角色配置")
@RestController
@RequestMapping("/data/role")
@RequiredArgsConstructor
public class DataRoleController {

    private final DataRoleService dataRoleService;

    private final DataRoleUserService dataRoleUserService;

    @Operation(summary = "添加")
    @PostMapping("/add")
    public ResResult<Void> add(@RequestBody DataRoleParam param) {
        dataRoleService.add(param);
        return Res.ok();
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public ResResult<Void> update(@RequestBody DataRoleParam param) {
        dataRoleService.update(param);
        return Res.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/delete")
    public ResResult<Void> delete(Long id) {
        dataRoleService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "保存关联部门")
    @PostMapping("/saveDeptAssign")
    public ResResult<Void> saveDeptAssign(@RequestBody DataRoleDeptParam param) {
        dataRoleService.saveDeptAssign(param);
        return Res.ok();
    }

    @Operation(summary = "获取关联部门id")
    @GetMapping("/getDeptIds")
    public ResResult<List<Long>> getDeptIds(Long id) {
        return Res.ok(dataRoleService.findDeptIds(id));
    }

    @Operation(summary = "编码是否被使用")
    @GetMapping("/existsByCode")
    public ResResult<Boolean> existsByCode(String code) {
        return Res.ok(dataRoleService.existsByCode(code));
    }

    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public ResResult<Boolean> existsByCode(String code, Long id) {
        return Res.ok(dataRoleService.existsByCode(code, id));
    }

    @Operation(summary = "名称是否被使用")
    @GetMapping("/existsByName")
    public ResResult<Boolean> existsByName(String name) {
        return Res.ok(dataRoleService.existsByName(name));
    }

    @Operation(summary = "名称是否被使用(不包含自己)")
    @GetMapping("/existsByNameNotId")
    public ResResult<Boolean> existsByName(String name, Long id) {
        return Res.ok(dataRoleService.existsByName(name, id));
    }

    @Operation(summary = "获取")
    @GetMapping("/findById")
    public ResResult<DataRoleDto> findById(Long id) {
        return Res.ok(dataRoleService.findById(id));
    }

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<DataRoleDto>> page(@ParameterObject PageParam pageParam,
                                                   @ParameterObject DataRoleParam param) {
        return Res.ok(dataRoleService.page(pageParam, param));
    }

    @Operation(summary = "查询全部")
    @GetMapping("/findAll")
    public ResResult<List<DataRoleDto>> findAll() {
        return Res.ok(dataRoleService.findAll());
    }

    @Operation(summary = "获取关联的用户列表")
    @GetMapping("/findUsersByDataRoleId")
    public ResResult<List<DataRoleUserDto>> findUsersByDataRoleId(Long id) {
        return Res.ok(dataRoleUserService.findUsersByDataRoleId(id));
    }

    @Operation(summary = "保存数据角色关联用户权限")
    @PostMapping("/saveUserAssign")
    public ResResult<Void> saveUserAssign(@RequestBody DataRoleUserParam param) {
        dataRoleUserService.saveUserAssign(param.getDataRoleId(), param.getUserIds());
        return Res.ok();
    }

    @Operation(summary = "批量删除数据角色关联用户")
    @DeleteMapping("/deleteUserAssigns")
    public ResResult<Void> deleteUserAssigns(@RequestBody List<Long> ids) {
        dataRoleUserService.deleteBatch(ids);
        return Res.ok();
    }

}
