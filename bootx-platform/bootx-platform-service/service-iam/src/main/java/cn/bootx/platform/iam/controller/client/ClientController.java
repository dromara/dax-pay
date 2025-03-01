package cn.bootx.platform.iam.controller.client;

import cn.bootx.platform.core.annotation.InternalPath;
import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import cn.bootx.platform.iam.param.client.ClientParam;
import cn.bootx.platform.iam.result.client.ClientResult;
import cn.bootx.platform.iam.service.client.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 认证终端
 *
 * @author xxm
 * @since 2022-06-27
 */
@Validated
@Tag(name = "认证终端")
@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
@RequestGroup(groupCode = "client", groupName = "终端管理", moduleCode = "iam", moduleName = "(Bootx)身份识别与访问管理" )
public class ClientController {

    private final ClientService clientService;

    @InternalPath
    @Operation(summary = "添加终端")
    @PostMapping(value = "/add")
    @OperateLog(title = "添加终端", businessType = OperateLog.BusinessType.ADD, saveParam = true)
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) ClientParam param) {
        clientService.add(param);
        return Res.ok();
    }

    @InternalPath
    @Operation(summary = "修改")
    @PostMapping(value = "/update")
    @OperateLog(title = "修改终端", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) ClientParam param) {
        clientService.update(param);
        return Res.ok();
    }

    @InternalPath
    @Operation(summary = "删除")
    @PostMapping(value = "/delete")
    @OperateLog(title = "删除终端", businessType = OperateLog.BusinessType.DELETE, saveParam = true)
    public Result<Void> delete(@NotNull(message = "主键不可为空") Long id) {
        clientService.delete(id);
        return Res.ok();
    }

    @RequestPath("通过ID查询终端")
    @Operation(summary = "通过ID查询")
    @GetMapping(value = "/findById")
    public Result<ClientResult> findById(@NotNull(message = "主键不可为空") Long id) {
        return Res.ok(clientService.findById(id));
    }

    @RequestPath("查询所有终端")
    @Operation(summary = "查询所有")
    @GetMapping(value = "/findAll")
    public Result<List<ClientResult>> findAll() {
        return Res.ok(clientService.findAll());
    }

    @RequestPath("分页查询终端")
    @Operation(summary = "分页查询")
    @GetMapping(value = "/page")
    public Result<PageResult<ClientResult>> page(PageParam pageParam, ClientParam clientParam) {
        return Res.ok(clientService.page(pageParam, clientParam));
    }

    @RequestPath("编码是否被使用")
    @Operation(summary = "编码是否被使用")
    @GetMapping("/existsByCode")
    public Result<Boolean> existsByCode(@NotBlank(message = "编码不可为空") @Parameter(description = "编码") String code) {
        return Res.ok(clientService.existsByCode(code));
    }

    @RequestPath("编码是否被使用(不包含自己)")
    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public Result<Boolean> existsByCode(
            @NotBlank(message = "编码不可为空") @Parameter(description = "编码") String code,
            @NotNull(message = "主键不可为空") @Parameter(description = "主键") Long id) {
        return Res.ok(clientService.existsByCode(code, id));
    }

}
