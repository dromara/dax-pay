package cn.daxpay.open.payment.app.merchant.controller.channel;

import cn.daxpay.open.payment.app.merchant.service.channel.AppMerchantChannelMerchantService;
import cn.daxpay.open.payment.merchant.param.channel.ChannelMerchantEditParam;
import cn.daxpay.open.payment.merchant.param.channel.ChannelMerchantQuery;
import cn.daxpay.open.payment.merchant.result.channel.ChannelMerchantReferenceResult;
import cn.daxpay.open.payment.merchant.result.channel.ChannelMerchantResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 通道商户管理(商户移动端)
///
/// 面向商户移动端的通道商户查询。业务编排委托 [AppMerchantChannelMerchantService]；
/// 商户号强制取自 [cn.daxpay.open.payment.common.context.PaymentContext]，防越权。
/// reference-detail 端点镜像 Web 商户端
/// [cn.daxpay.open.payment.merchant.controller.channel.MchChannelMerchantController] 的同名引用明细查询,
/// 复用同一核心 Service, 无业务逻辑。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "通道商户管理(商户移动端)")
@RestController
@RequestMapping("/app-mch/channel-merchant")
@RequiredArgsConstructor
public class AppMerchantChannelMerchantController {

    private final AppMerchantChannelMerchantService channelMerchantService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<ChannelMerchantResult>> page(PageParam pageParam, ChannelMerchantQuery query) {
        return Res.ok(channelMerchantService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询详情")
    @GetMapping("/get")
    public Result<ChannelMerchantResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(channelMerchantService.findById(id));
    }

    /// 查询当前商户全部通道商户（商户号取登录上下文，通道路由选择弹层候选）
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询当前商户全部通道商户")
    @GetMapping("/all")
    public Result<List<ChannelMerchantResult>> findAllOfCurrentMch() {
        return Res.ok(channelMerchantService.findAllOfCurrentMch());
    }

    /// 引用明细查询（按应用分组；非本商户的通道商户号在服务层即查不到，天然防越权）
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "引用明细查询(按应用分组, 停用影响面提示用)")
    @GetMapping("/reference-detail")
    public Result<ChannelMerchantReferenceResult> referenceDetail(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(channelMerchantService.referenceDetail(channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新启用状态")
    @PostMapping("/update-enable")
    public Result<Void> updateEnable(@NotNull(message = "{validation.field.id.notNull}") Long id,
                                     @NotNull(message = "{validation.field.enable.notNull}") Boolean enable) {
        // 归属校验在 Service 内（先查后校再改状态）
        channelMerchantService.updateEnable(id, enable);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改商户名称")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated ChannelMerchantEditParam param) {
        channelMerchantService.update(param);
        return Res.ok();
    }
}
