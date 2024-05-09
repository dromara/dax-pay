package cn.bootx.platform.iam.controller;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.iam.core.dept.service.DeptService;
import cn.bootx.platform.iam.dto.dept.DeptDto;
import cn.bootx.platform.iam.dto.dept.DeptTreeResult;
import cn.bootx.platform.iam.param.dept.DeptParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xxm
 * @since 2020/5/10
 */
@Tag(name = "部门管理")
@RestController
@RequestMapping("/dept")
@AllArgsConstructor
public class DeptController {

    private final DeptService deptService;

    @Operation(summary = "添加")
    @PostMapping("/add")
    public ResResult<DeptDto> add(@RequestBody DeptParam param) {
        return Res.ok(deptService.add(param));
    }

    @Operation(summary = "获取")
    @GetMapping("/findById")
    ResResult<DeptDto> findById(Long id) {
        return Res.ok(deptService.findById(id));
    }

    @Operation(summary = "树状展示")
    @GetMapping("/tree")
    public ResResult<List<DeptTreeResult>> tree() {
        return Res.ok(deptService.tree());
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public ResResult<DeptDto> update(@RequestBody DeptParam param) {
        return Res.ok(deptService.update(param));
    }

    @Operation(summary = "普通删除")
    @DeleteMapping("/delete")
    public ResResult<Void> delete(Long id) {
        deptService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "强制级联删除")
    @DeleteMapping("/deleteAndChildren")
    public ResResult<Boolean> deleteAndChildren(Long id) {
        return Res.ok(deptService.deleteAndChildren(id));
    }

}
