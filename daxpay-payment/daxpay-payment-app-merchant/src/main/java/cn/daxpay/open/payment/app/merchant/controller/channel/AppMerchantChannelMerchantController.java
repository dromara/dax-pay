package cn.daxpay.open.payment.app.merchant.controller.channel;

import cn.daxpay.open.payment.app.merchant.service.channel.AppMerchantChannelMerchantService;
import cn.daxpay.open.payment.merchant.param.channel.ChannelMerchantQuery;
import cn.daxpay.open.payment.merchant.result.channel.ChannelMerchantResult;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 通道商户管理(商户移动端)
///
/// 面向商户移动端的通道商户查询。业务编排委托 [AppMerchantChannelMerchantService]；
/// 商户号强制取自 PaymentContext，防越权。
@Validated
@Tag(name = "通道商户管理(商户移动端)")
@RestController
@RequestMapping("/app-merchant/channel-merchant")
@RequiredArgsConstructor
public class AppMerchantChannelMerchantController {

    private final AppMerchantChannelMerchantService channelMerchantService;

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<ChannelMerchantResult>> page(PageParam pageParam, ChannelMerchantQuery query) {
        return Res.ok(channelMerchantService.page(pageParam, query));
    }

    @Operation(summary = "查询详情")
    @GetMapping("/get")
    public Result<ChannelMerchantResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(channelMerchantService.findById(id));
    }
}
