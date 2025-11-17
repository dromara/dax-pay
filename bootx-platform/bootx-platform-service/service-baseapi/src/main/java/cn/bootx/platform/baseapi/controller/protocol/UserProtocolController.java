package cn.bootx.platform.baseapi.controller.protocol;

import cn.bootx.platform.baseapi.param.protocol.UserProtocolParam;
import cn.bootx.platform.baseapi.param.protocol.UserProtocolQuery;
import cn.bootx.platform.baseapi.result.protocol.UserProtocolResult;
import cn.bootx.platform.baseapi.service.protocol.UserProtocolService;
import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户协议
 * @author xxm
 * @since 2025/5/9
 */
@Validated
@Tag(name = "用户协议")
@RequestGroup(groupCode = "UserProtocol", groupName = "用户协议", moduleCode = "baseapi" )
@RestController
@RequestMapping("/user/protocol")
@RequiredArgsConstructor
public class UserProtocolController {
    private final UserProtocolService userProtocolService;

    @RequestPath("分页")
    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<UserProtocolResult>> page(PageParam pageParam, UserProtocolQuery query){
        return Res.ok(userProtocolService.page(pageParam, query));
    }

    @RequestPath("新增")
    @Operation(summary = "新增")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody  @Validated(ValidationGroup.add.class) UserProtocolParam param){
        userProtocolService.add(param);
        return Res.ok();
    }

    @RequestPath("修改")
    @Operation(summary = "修改")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody  @Validated(ValidationGroup.edit.class) UserProtocolParam param){
        userProtocolService.update(param);
        return Res.ok();
    }

    @RequestPath("删除")
    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "主键不可为空") Long id){
        userProtocolService.delete(id);
        return Res.ok();
    }

    @RequestPath("查询")
    @Operation(summary = "查询")
    @GetMapping("/findById")
    public Result<UserProtocolResult> findById(@NotNull(message = "主键不可为空") Long id){
        return Res.ok(userProtocolService.findById(id));
    }

    @IgnoreAuth
    @Operation(summary = "查询默认协议")
    @GetMapping("/findDefault")
    public Result<UserProtocolResult> findDefault(@NotNull(message = "协议类型不可为空") String type){
        return Res.ok(userProtocolService.findDefault(type));
    }

    @RequestPath("设置默认")
    @Operation(summary = "设置默认")
    @PostMapping("/setDefault")
    public Result<Void> setDefault(@NotNull(message = "主键不可为空") Long id){
        userProtocolService.setDefault(id);
        return Res.ok();
    }

    @RequestPath("取消默认")
    @Operation(summary = "取消默认")
    @PostMapping("/cancelDefault")
    public Result<Void> cancelDefault(@NotNull(message = "主键不可为空") Long id){
        userProtocolService.cancelDefault(id);
        return Res.ok();
    }

}
