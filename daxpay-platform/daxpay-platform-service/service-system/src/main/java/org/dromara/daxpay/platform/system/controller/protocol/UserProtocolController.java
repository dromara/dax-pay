package org.dromara.daxpay.platform.system.controller.protocol;

import org.dromara.daxpay.platform.system.enums.UserProtocolClientTypeEnum;
import org.dromara.daxpay.platform.system.enums.UserProtocolTypeEnum;
import org.dromara.daxpay.platform.system.param.protocol.UserProtocolParam;
import org.dromara.daxpay.platform.system.param.protocol.UserProtocolQuery;
import org.dromara.daxpay.platform.system.result.protocol.UserProtocolResult;
import org.dromara.daxpay.platform.system.service.protocol.UserProtocolService;
import org.dromara.daxpay.platform.core.annotation.IgnoreAuth;
import org.dromara.daxpay.platform.common.i18n.util.I18nUtil;
import org.dromara.daxpay.platform.core.rest.dto.LabelValue;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/// # 用户协议控制器
///
@Validated
@Tag(name = "用户协议")
@RestController
@RequestMapping("/user/protocol")
@RequiredArgsConstructor
public class UserProtocolController {
    private final UserProtocolService userProtocolService;

    /// 分页查询用户协议
    ///
    /// @param pageParam 分页参数
    /// @param query 查询条件
    /// @return 用户协议分页结果
    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<UserProtocolResult>> page(PageParam pageParam, UserProtocolQuery query){
        return Res.ok(userProtocolService.page(pageParam, query));
    }

    /// 新增用户协议
    ///
    /// @param param 用户协议参数
    /// @return 操作结果
    @Operation(summary = "新增")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody  @Validated(ValidationGroup.add.class) UserProtocolParam param){
        userProtocolService.add(param);
        return Res.ok();
    }

    /// 修改用户协议
    ///
    /// @param param 用户协议参数
    /// @return 操作结果
    @Operation(summary = "修改")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody  @Validated(ValidationGroup.edit.class) UserProtocolParam param){
        userProtocolService.update(param);
        return Res.ok();
    }

    /// 删除用户协议
    ///
    /// @param id 协议ID
    /// @return 操作结果
    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id){
        userProtocolService.delete(id);
        return Res.ok();
    }

    /// 根据ID查询用户协议
    ///
    /// @param id 协议ID
    /// @return 用户协议信息
    @Operation(summary = "查询")
    @GetMapping("/get")
    public Result<UserProtocolResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id){
        return Res.ok(userProtocolService.findById(id));
    }

    /// 查询默认协议
    ///
    /// @param type 协议类型
    /// @return 默认协议信息
    @IgnoreAuth
    @Operation(summary = "查询默认协议")
    @GetMapping("/find-default")
    public Result<UserProtocolResult> findDefault(@NotNull(message = "{validation.field.type.notBlank}") String type,
        @NotNull(message = "{validation.field.clientType.notBlank}") String clientType){
        return Res.ok(userProtocolService.findDefault(type, clientType));
    }

    @Operation(summary = "协议类型列表")
    @GetMapping("/type-options")
    public Result<List<LabelValue>> typeOptions(){
        return Res.ok(Arrays.stream(UserProtocolTypeEnum.values())
                .map(v -> new LabelValue(I18nUtil.getEnumName(v), v.getCode()))
                .toList());
    }

    @Operation(summary = "协议端类型列表")
    @GetMapping("/client-type-options")
    public Result<List<LabelValue>> clientTypeOptions(){
        return Res.ok(Arrays.stream(UserProtocolClientTypeEnum.values())
                .map(v -> new LabelValue(I18nUtil.getEnumName(v), v.getCode()))
                .toList());
    }

    /// 设置默认协议
    ///
    /// @param id 协议ID
    /// @return 操作结果
    @Operation(summary = "设置默认")
    @PostMapping("/set-default")
    public Result<Void> setDefault(@NotNull(message = "{validation.field.id.notNull}") Long id){
        userProtocolService.setDefault(id);
        return Res.ok();
    }

    /// 取消默认协议
    ///
    /// @param id 协议ID
    /// @return 操作结果
    @Operation(summary = "取消默认")
    @PostMapping("/cancel-default")
    public Result<Void> cancelDefault(@NotNull(message = "{validation.field.id.notNull}") Long id){
        userProtocolService.cancelDefault(id);
        return Res.ok();
    }

}


