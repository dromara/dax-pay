package cn.daxpay.open.channel.mock.controller.admin;

import cn.daxpay.open.channel.mock.param.isv.MockIsvInfoParam;
import cn.daxpay.open.channel.mock.result.isv.MockIsvInfoListResult;
import cn.daxpay.open.channel.mock.result.isv.MockIsvInfoOption;
import cn.daxpay.open.channel.mock.result.isv.MockIsvInfoResult;
import cn.daxpay.open.channel.mock.service.isv.MockIsvInfoService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # Mock 模拟服务商信息管理
///
/// 模拟通道服务商(ISV)模式的主数据维护入口(服务商建档 + 编辑启停 + 删除),
/// 权限用法与 [MockChannelMerchantController] 同款(复用通道商户通用菜单 + 动作码)。
/// 随 [cn.daxpay.open.channel.mock.MockChannelApp] 仅 DEV 装配, PROD 下端点不存在。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Tag(name = "Mock模拟服务商信息管理")
@RestController
@RequestMapping("/admin/mock/isv-info")
@RequiredArgsConstructor
@Validated
public class MockIsvInfoController {

    private final MockIsvInfoService mockIsvInfoService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "服务商下拉(全部行含停用)")
    @GetMapping("/dropdown")
    public Result<List<MockIsvInfoOption>> dropdown() {
        return Res.ok(mockIsvInfoService.dropdown());
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "服务商全量列表(模拟服务商管理页, 含名下通道商户数)")
    @GetMapping("/list")
    public Result<List<MockIsvInfoListResult>> list() {
        return Res.ok(mockIsvInfoService.listWithCount());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建Mock模拟服务商")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated MockIsvInfoParam param) {
        mockIsvInfoService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "编辑Mock模拟服务商(编辑+启停合一)")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated MockIsvInfoParam param) {
        // 编辑与启停合一: 主键必填(create 与 update 合用同一参数对象, 新增侧 id 为空不走本端点校验)
        if (param.getId() == null) {
            // 主键不可为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "validation.field.id.notNull");
        }
        mockIsvInfoService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询详情")
    @GetMapping("/find")
    public Result<MockIsvInfoResult> find(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(mockIsvInfoService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除服务商")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        mockIsvInfoService.delete(id);
        return Res.ok();
    }
}
