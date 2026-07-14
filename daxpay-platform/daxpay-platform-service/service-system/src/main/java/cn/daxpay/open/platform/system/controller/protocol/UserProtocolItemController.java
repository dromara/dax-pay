package cn.daxpay.open.platform.system.controller.protocol;

import cn.daxpay.open.platform.system.param.protocol.UserProtocolItemParam;
import cn.daxpay.open.platform.system.param.protocol.UserProtocolItemQuery;
import cn.daxpay.open.platform.system.result.protocol.UserProtocolItemResult;
import cn.daxpay.open.platform.system.service.protocol.UserProtocolItemService;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 用户协议项控制器
///
@Validated
@Tag(name = "用户协议项")
@RestController
@RequestMapping("/user/protocol/item")
@RequiredArgsConstructor
public class UserProtocolItemController {
    private final UserProtocolItemService userProtocolItemService;

    /// 分页查询用户协议项
    ///
    /// @param pageParam 分页参数
    /// @param query 查询条件
    /// @return 用户协议项分页结果
    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<UserProtocolItemResult>> page(PageParam pageParam, UserProtocolItemQuery query){
        return Res.ok(userProtocolItemService.page(pageParam, query));
    }

    /// 新增用户协议项
    ///
    /// @param param 用户协议项参数
    /// @return 操作结果
    @Operation(summary = "新增")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody  @Validated(ValidationGroup.add.class) UserProtocolItemParam param){
        userProtocolItemService.add(param);
        return Res.ok();
    }

    /// 修改用户协议项
    ///
    /// @param param 用户协议项参数
    /// @return 操作结果
    @Operation(summary = "修改")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody  @Validated(ValidationGroup.edit.class) UserProtocolItemParam param){
        userProtocolItemService.update(param);
        return Res.ok();
    }

    /// 删除用户协议项
    ///
    /// @param id 协议项ID
    /// @return 操作结果
    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id){
        userProtocolItemService.delete(id);
        return Res.ok();
    }

    /// 根据ID查询用户协议项
    ///
    /// @param id 协议项ID
    /// @return 用户协议项信息
    @Operation(summary = "查询")
    @GetMapping("/get")
    public Result<UserProtocolItemResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id){
        return Res.ok(userProtocolItemService.findById(id));
    }

    /// 根据协议类型查询协议项
    ///
    /// @param type 协议类型
    /// @return 协议项列表
    @Operation(summary = "根据协议类型查询协议项")
    @GetMapping("/get-by-protocol-type")
    public Result<List<UserProtocolItemResult>> findByProtocolType(@NotNull(message = "{validation.field.type.notBlank}") String type,
        @NotNull(message = "{validation.field.clientType.notBlank}") String clientType){
        return Res.ok(userProtocolItemService.findByProtocolType(type, clientType));
    }
}

