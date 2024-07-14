package cn.bootx.platform.iam.controller.client;

import cn.bootx.platform.core.annotation.InternalPath;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.iam.param.client.ClientParam;
import cn.bootx.platform.iam.result.client.ClientResult;
import cn.bootx.platform.iam.service.client.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 认证终端
 *
 * @author xxm
 * @since 2022-06-27
 */
@Tag(name = "认证终端")
@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
@RequestGroup(groupCode = "client", groupName = "终端管理", moduleCode = "iam", moduleName = "身份识别与访问管理" )
public class ClientController {

    private final ClientService clientService;

    @InternalPath
    @Operation(summary = "添加终端")
    @PostMapping(value = "/add")
    public Result<Void> add(@RequestBody ClientParam param) {
        clientService.add(param);
        return Res.ok();
    }

    @InternalPath
    @Operation(summary = "修改")
    @PostMapping(value = "/update")
    public Result<Void> update(@RequestBody ClientParam param) {
        clientService.update(param);
        return Res.ok();
    }

    @InternalPath
    @Operation(summary = "删除")
    @PostMapping(value = "/delete")
    public Result<Void> delete(Long id) {
        clientService.delete(id);
        return Res.ok();
    }

    @RequestPath("通过ID查询终端")
    @Operation(summary = "通过ID查询")
    @GetMapping(value = "/findById")
    public Result<ClientResult> findById(Long id) {
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
    public Result<Boolean> existsByCode(String code) {
        return Res.ok(clientService.existsByCode(code));
    }

    @RequestPath("编码是否被使用(不包含自己)")
    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public Result<Boolean> existsByCode(String code, Long id) {
        return Res.ok(clientService.existsByCode(code, id));
    }

}
