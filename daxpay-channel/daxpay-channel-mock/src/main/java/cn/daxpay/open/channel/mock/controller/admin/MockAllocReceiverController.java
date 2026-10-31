package cn.daxpay.open.channel.mock.controller.admin;

import cn.daxpay.open.channel.mock.param.alloc.MockAllocReceiverBindParam;
import cn.daxpay.open.channel.mock.param.alloc.MockAllocReceiverCreateParam;
import cn.daxpay.open.channel.mock.param.alloc.MockAllocReceiverQuery;
import cn.daxpay.open.channel.mock.result.alloc.MockAllocReceiverResult;
import cn.daxpay.open.channel.mock.service.alloc.MockAllocReceiverAdminService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # Mock 模拟通道分账接收方管理
///
/// 挂通道商户详情页的子功能, 接收方在通道侧注册后方可在分账中接收资金;
/// 形态对齐 [cn.daxpay.open.channel.alipay.controller.direct.AlipayDirectAllocReceiverController],
/// 差异是 mock 无应用维度(不选 appid), 且两产品共用同一套端点(产品由通道商户反解)。
/// 复用通道商户通用权限码(菜单 channel:merchant + 动作码), 无新增菜单/权限种子。
/// 随 [cn.daxpay.open.channel.mock.MockChannelApp] 仅 DEV 装配, PROD 下端点不存在。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "Mock模拟通道分账接收方管理")
@RestController
@RequestMapping("/admin/mock/alloc-receiver")
@RequiredArgsConstructor
public class MockAllocReceiverController {

    private final MockAllocReceiverAdminService mockAllocReceiverAdminService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<MockAllocReceiverResult>> page(PageParam pageParam, MockAllocReceiverQuery query) {
        return Res.ok(mockAllocReceiverAdminService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增并绑定接收方(同步调通道, 失败记录保留)")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated MockAllocReceiverCreateParam param) {
        mockAllocReceiverAdminService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "重新绑定(绑定失败/已解绑状态)")
    @PostMapping("/bind")
    public Result<Void> bind(@RequestBody @Validated MockAllocReceiverBindParam param) {
        mockAllocReceiverAdminService.bind(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "解绑(已绑定状态, 保留记录)")
    @PostMapping("/unbind")
    public Result<Void> unbind(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        mockAllocReceiverAdminService.unbind(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除(仅绑定失败/已解绑状态)")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        mockAllocReceiverAdminService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改别名(本地备注, 不上送通道; 留空即清空)")
    @PostMapping("/update-alias")
    public Result<Void> updateAlias(@NotNull(message = "{validation.field.id.notNull}") Long id,
                                    @Size(max = 50, message = "{validation.field.alias.size}") String alias) {
        mockAllocReceiverAdminService.updateAlias(id, alias);
        return Res.ok();
    }
}
