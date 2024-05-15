package cn.bootx.platform.iam.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.query.entity.QueryParams;
import cn.bootx.platform.iam.core.client.service.LoginTypeService;
import cn.bootx.platform.iam.dto.client.LoginTypeDto;
import cn.bootx.platform.iam.param.client.LoginTypeParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 登录方式
 *
 * @author xxm
 * @since 2021/8/25
 */
@Tag(name = "登录方式管理")
@RestController
@RequestMapping("/loginType")
@RequiredArgsConstructor
public class LoginTypeController {

    private final LoginTypeService loginTypeService;

    @Operation(summary = "添加登录方式")
    @PostMapping(value = "/add")
    public ResResult<LoginTypeDto> add(@RequestBody LoginTypeParam param) {
        return Res.ok(loginTypeService.add(param));
    }

    @Operation(summary = "修改登录方式（返回登录方式对象）")
    @PostMapping(value = "/update")
    public ResResult<LoginTypeDto> update(@RequestBody LoginTypeParam param) {
        return Res.ok(loginTypeService.update(param));
    }

    @Operation(summary = "删除登录方式")
    @DeleteMapping(value = "/delete")
    public ResResult<Void> delete(Long id) {
        loginTypeService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "通过ID查询登录方式")
    @GetMapping(value = "/findById")
    public ResResult<LoginTypeDto> findById(Long id) {
        return Res.ok(loginTypeService.findById(id));
    }

    @IgnoreAuth
    @Operation(summary = "通过code查询登录方式")
    @GetMapping(value = "/findByCode")
    public ResResult<LoginTypeDto> findByCode(String code) {
        return Res.ok(loginTypeService.findByCode(code));
    }

    @Operation(summary = "查询所有的登录方式")
    @GetMapping(value = "/findAll")
    public ResResult<List<LoginTypeDto>> findAll() {
        return Res.ok(loginTypeService.findAll());
    }

    @Operation(summary = "分页查询登录方式")
    @GetMapping(value = "/page")
    public ResResult<PageResult<LoginTypeDto>> page(PageParam pageParam, LoginTypeParam loginTypeParam) {
        return Res.ok(loginTypeService.page(pageParam, loginTypeParam));
    }

    @Operation(summary = "超级查询")
    @PostMapping("/superPage")
    public ResResult<PageResult<LoginTypeDto>> superPage(PageParam pageParam, @RequestBody QueryParams queryParams) {
        return Res.ok(loginTypeService.superPage(pageParam, queryParams));
    }

    @Operation(summary = "编码是否被使用")
    @GetMapping("/existsByCode")
    public ResResult<Boolean> existsByCode(String code) {
        return Res.ok(loginTypeService.existsByCode(code));
    }

    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public ResResult<Boolean> existsByCode(String code, Long id) {
        return Res.ok(loginTypeService.existsByCode(code, id));
    }

}
