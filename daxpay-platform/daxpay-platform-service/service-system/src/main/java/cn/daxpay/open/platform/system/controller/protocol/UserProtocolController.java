package cn.daxpay.open.platform.system.controller.protocol;

import cn.daxpay.open.platform.system.enums.UserProtocolClientTypeEnum;
import cn.daxpay.open.platform.system.enums.UserProtocolTypeEnum;
import cn.daxpay.open.platform.system.param.protocol.UserProtocolParam;
import cn.daxpay.open.platform.system.param.protocol.UserProtocolQuery;
import cn.daxpay.open.platform.system.result.protocol.UserProtocolContentResult;
import cn.daxpay.open.platform.system.result.protocol.UserProtocolResult;
import cn.daxpay.open.platform.system.service.protocol.UserProtocolService;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
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

import java.util.Arrays;
import java.util.List;

/// # 用户协议控制器
///
@PermCode(menuCode = "system:protocol")
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
    @PermCode(code = "view", nameCn = "协议查看", nameEn = "Protocol View")
    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<UserProtocolResult>> page(PageParam pageParam, UserProtocolQuery query){
        return Res.ok(userProtocolService.page(pageParam, query));
    }

    /// 新增用户协议
    ///
    /// @param param 用户协议参数
    /// @return 操作结果
    @PermCode(code = "manage", nameCn = "协议管理", nameEn = "Protocol Manage")
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
    @PermCode(code = "manage", nameCn = "协议管理", nameEn = "Protocol Manage")
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
    @PermCode(code = "manage", nameCn = "协议管理", nameEn = "Protocol Manage")
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
    @PermCode(code = "view", nameCn = "协议查看", nameEn = "Protocol View")
    @Operation(summary = "查询")
    @GetMapping("/get")
    public Result<UserProtocolResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id){
        return Res.ok(userProtocolService.findById(id));
    }

    /// 查询默认协议内容(对外展示, 各端通过 type+clientType+language 拉取当前生效版本)
    ///
    /// @param type 协议类型
    /// @param clientType 端类型
    /// @param language 语言(可选, 为空时回退到协议默认语言)
    /// @return 协议内容
    @IgnoreAuth
    @Operation(summary = "查询默认协议内容")
    @GetMapping("/find-default")
    public Result<UserProtocolContentResult> findDefault(@NotNull(message = "{validation.field.type.notBlank}") String type,
        @NotNull(message = "{validation.field.clientType.notBlank}") String clientType,
        @RequestParam(required = false) String language){
        return Res.ok(userProtocolService.findDefault(type, clientType, language));
    }

    @PermCode(code = "view", nameCn = "协议查看", nameEn = "Protocol View")
    @Operation(summary = "协议类型列表")
    @GetMapping("/type-options")
    public Result<List<LabelValue>> typeOptions(){
        return Res.ok(Arrays.stream(UserProtocolTypeEnum.values())
                .map(v -> new LabelValue(I18nUtil.getEnumName(v), v.getCode()))
                .toList());
    }

    @PermCode(code = "view", nameCn = "协议查看", nameEn = "Protocol View")
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
    @PermCode(code = "manage", nameCn = "协议管理", nameEn = "Protocol Manage")
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
    @PermCode(code = "manage", nameCn = "协议管理", nameEn = "Protocol Manage")
    @Operation(summary = "取消默认")
    @PostMapping("/cancel-default")
    public Result<Void> cancelDefault(@NotNull(message = "{validation.field.id.notNull}") Long id){
        userProtocolService.cancelDefault(id);
        return Res.ok();
    }

    /// 复制协议到其他端(连同各语言的当前生效版本一起复制)
    ///
    /// @param id 源协议ID
    /// @param clientType 目标端类型
    /// @return 目标协议ID
    @PermCode(code = "manage", nameCn = "协议管理", nameEn = "Protocol Manage")
    @Operation(summary = "复制到其他端")
    @PostMapping("/copy-to-client")
    public Result<Long> copyToClient(@NotNull(message = "{validation.field.id.notNull}") Long id,
        @NotNull(message = "{validation.field.clientType.notBlank}") String clientType){
        return Res.ok(userProtocolService.copyToClient(id, clientType));
    }

}
