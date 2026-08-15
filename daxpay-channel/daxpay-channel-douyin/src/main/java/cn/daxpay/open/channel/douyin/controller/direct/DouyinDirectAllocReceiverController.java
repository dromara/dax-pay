package cn.daxpay.open.channel.douyin.controller.direct;

import cn.daxpay.open.channel.douyin.param.direct.DouyinDirectAllocReceiverBindParam;
import cn.daxpay.open.channel.douyin.param.direct.DouyinDirectAllocReceiverCreateParam;
import cn.daxpay.open.channel.douyin.param.direct.DouyinDirectAllocReceiverQuery;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectAllocReceiverResult;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectAllocReceiverService;
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

/// # 抖音直连分账接收方管理
///
/// 挂通道商户(进件商户)详情页的子功能, 接收方在通道侧注册后方可在分账中接收资金。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "抖音直连分账接收方管理")
@RestController
@RequestMapping("/admin/douyin/direct-alloc-receiver")
@RequiredArgsConstructor
public class DouyinDirectAllocReceiverController {

    private final DouyinDirectAllocReceiverService douyinDirectAllocReceiverService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<DouyinDirectAllocReceiverResult>> page(PageParam pageParam,
                                                                    DouyinDirectAllocReceiverQuery query) {
        return Res.ok(douyinDirectAllocReceiverService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增并绑定接收方(同步调通道, 失败记录保留)")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated DouyinDirectAllocReceiverCreateParam param) {
        douyinDirectAllocReceiverService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "重新绑定(绑定失败/已解绑状态, 可更换绑定所用应用)")
    @PostMapping("/bind")
    public Result<Void> bind(@RequestBody @Validated DouyinDirectAllocReceiverBindParam param) {
        douyinDirectAllocReceiverService.bind(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "解绑(已绑定状态, 保留记录)")
    @PostMapping("/unbind")
    public Result<Void> unbind(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        douyinDirectAllocReceiverService.unbind(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除(仅绑定失败/已解绑状态)")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        douyinDirectAllocReceiverService.delete(id);
        return Res.ok();
    }
}
