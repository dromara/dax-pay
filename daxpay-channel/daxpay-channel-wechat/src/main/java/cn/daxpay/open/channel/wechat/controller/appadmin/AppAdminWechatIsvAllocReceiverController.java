package cn.daxpay.open.channel.wechat.controller.appadmin;

import cn.daxpay.open.channel.wechat.param.isv.WechatIsvAllocReceiverBindParam;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvAllocReceiverCreateParam;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvAllocReceiverQuery;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvAllocReceiverResult;
import cn.daxpay.open.channel.wechat.service.isv.WechatIsvAllocReceiverService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
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

/// # 微信服务商分账接收方管理(小程序管理端镜像)
///
/// 对应 admin 版 [WechatIsvAllocReceiverController], 全端点镜像, 复用同一 Service 与权限码。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "小程序管理端-微信服务商分账接收方管理")
@RestController
@RequestMapping("/app-admin/wechat/isv-alloc-receiver")
@RequiredArgsConstructor
public class AppAdminWechatIsvAllocReceiverController {

    private final WechatIsvAllocReceiverService wechatIsvAllocReceiverService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<WechatIsvAllocReceiverResult>> page(PageParam pageParam,
                                                                 WechatIsvAllocReceiverQuery query) {
        return Res.ok(wechatIsvAllocReceiverService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增并绑定接收方(同步调通道, 失败记录保留)")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated WechatIsvAllocReceiverCreateParam param) {
        wechatIsvAllocReceiverService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "重新绑定(绑定失败/已解绑状态, 可更换绑定所用应用)")
    @PostMapping("/bind")
    public Result<Void> bind(@RequestBody @Validated WechatIsvAllocReceiverBindParam param) {
        wechatIsvAllocReceiverService.bind(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "解绑(已绑定状态, 保留记录)")
    @PostMapping("/unbind")
    public Result<Void> unbind(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        wechatIsvAllocReceiverService.unbind(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除(仅绑定失败/已解绑状态)")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        wechatIsvAllocReceiverService.delete(id);
        return Res.ok();
    }
}
