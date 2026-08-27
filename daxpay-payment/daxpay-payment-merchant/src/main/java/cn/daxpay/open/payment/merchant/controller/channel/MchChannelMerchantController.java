package cn.daxpay.open.payment.merchant.controller.channel;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.param.channel.ChannelMerchantEditParam;
import cn.daxpay.open.payment.merchant.param.channel.ChannelMerchantQuery;
import cn.daxpay.open.payment.merchant.result.channel.ChannelMerchantResult;
import cn.daxpay.open.payment.merchant.service.channel.ChannelMerchantService;
import cn.daxpay.open.payment.masterdata.result.channel.PayChannelResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.config.ConfigErrorException;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/// # 通道商户管理（商户端）
///
/// 对照运营端 [MerchantChannelMerchantAdminController]，路径前缀 `/mch/channel-merchant`。
/// 商户号一律取自 [PaymentContext]，忽略请求中的 mchNo，防越权。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "通道商户管理(商户端)")
@RestController
@RequestMapping("/mch/channel-merchant")
@RequiredArgsConstructor
public class MchChannelMerchantController {

    private final ChannelMerchantService channelMerchantService;
    private final PaymentContext paymentContext;

    /// 当前登录商户号（上下文必有；缺则视为会话异常）
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (mchNo == null || mchNo.isBlank()) {
            // 商户上下文缺失
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    /// 校验资源归属当前商户（TenantLine 兜底之外的显式防越权）
    private void assertOwned(ChannelMerchantResult result) {
        if (!Objects.equals(result.getMchNo(), requireMchNo())) {
            // 通道商户不属于当前商户（复用通用归属校验文案）
            throw new ConfigErrorException("error.payment.merchant.storeNoMatch");
        }
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<ChannelMerchantResult>> page(PageParam pageParam, ChannelMerchantQuery query) {
        // 强制当前商户，忽略客户端传入的 mchNo
        query.setMchNo(requireMchNo());
        return Res.ok(channelMerchantService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询详情")
    @GetMapping("/get")
    public Result<ChannelMerchantResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        ChannelMerchantResult result = channelMerchantService.findById(id);
        this.assertOwned(result);
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "当前商户下全部通道商户")
    @GetMapping("/all")
    public Result<List<ChannelMerchantResult>> findAll() {
        return Res.ok(channelMerchantService.findAllByMchNo(requireMchNo()));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新启用状态")
    @PostMapping("/update-enable")
    public Result<Void> updateEnable(@NotNull(message = "{validation.field.id.notNull}") Long id,
                                     @NotNull(message = "{validation.field.enable.notNull}") Boolean enable) {
        // 先校验归属再改状态
        this.assertOwned(channelMerchantService.findById(id));
        channelMerchantService.updateEnable(id, enable);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改商户名称")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated ChannelMerchantEditParam param) {
        this.assertOwned(channelMerchantService.findById(param.getId()));
        channelMerchantService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除通道商户")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        this.assertOwned(channelMerchantService.findById(id));
        channelMerchantService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "通道商户下拉（按产品）")
    @GetMapping("/dropdown")
    public Result<List<LabelValue>> dropdown(@NotBlank(message = "{validation.field.channel.notBlank}") String channel) {
        return Res.ok(channelMerchantService.dropdown(requireMchNo(), channel));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "当前商户已开通通道下拉")
    @GetMapping("/channel/dropdown")
    public Result<List<PayChannelResult>> dropdownChannels() {
        return Res.ok(channelMerchantService.dropdownByMchNo(requireMchNo()));
    }
}
