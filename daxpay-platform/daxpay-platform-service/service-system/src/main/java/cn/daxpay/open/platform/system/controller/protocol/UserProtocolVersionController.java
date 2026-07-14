package cn.daxpay.open.platform.system.controller.protocol;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import cn.daxpay.open.platform.system.param.protocol.UserProtocolVersionParam;
import cn.daxpay.open.platform.system.param.protocol.UserProtocolVersionQuery;
import cn.daxpay.open.platform.system.result.protocol.UserProtocolVersionResult;
import cn.daxpay.open.platform.system.service.protocol.UserProtocolVersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 用户协议版本控制器
///
@PermCode(menuCode = PermCodes.System.Protocol.MENU)
@Validated
@Tag(name = "用户协议版本")
@RestController
@RequestMapping("/user/protocol/version")
@RequiredArgsConstructor
public class UserProtocolVersionController {
    private final UserProtocolVersionService userProtocolVersionService;

    /// 分页查询版本
    ///
    /// @param pageParam 分页参数
    /// @param query 查询条件
    /// @return 版本分页结果
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<UserProtocolVersionResult>> page(PageParam pageParam, UserProtocolVersionQuery query){
        return Res.ok(userProtocolVersionService.page(pageParam, query));
    }

    /// 新建草稿
    ///
    /// @param param 版本参数
    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新建草稿")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) UserProtocolVersionParam param){
        userProtocolVersionService.add(param);
        return Res.ok();
    }

    /// 编辑草稿内容(仅草稿可编辑)
    ///
    /// @param param 版本参数
    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "编辑草稿")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) UserProtocolVersionParam param){
        userProtocolVersionService.update(param);
        return Res.ok();
    }

    /// 删除草稿(仅草稿可删除)
    ///
    /// @param id 版本ID
    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除草稿")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id){
        userProtocolVersionService.delete(id);
        return Res.ok();
    }

    /// 根据ID查询版本
    ///
    /// @param id 版本ID
    /// @return 版本信息
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询")
    @GetMapping("/get")
    public Result<UserProtocolVersionResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id){
        return Res.ok(userProtocolVersionService.findById(id));
    }

    /// 发布版本(草稿 -> 已发布, 同协议同语言原已发布自动归档)
    ///
    /// @param id 版本ID
    @PermCode(code = PermCodes.Action.PUBLISH)
    @Operation(summary = "发布版本")
    @PostMapping("/publish")
    public Result<Void> publish(@NotNull(message = "{validation.field.id.notNull}") Long id){
        userProtocolVersionService.publish(id);
        return Res.ok();
    }

    /// 归档版本(已发布 -> 归档)
    ///
    /// @param id 版本ID
    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "归档版本")
    @PostMapping("/archive")
    public Result<Void> archive(@NotNull(message = "{validation.field.id.notNull}") Long id){
        userProtocolVersionService.archive(id);
        return Res.ok();
    }
}
