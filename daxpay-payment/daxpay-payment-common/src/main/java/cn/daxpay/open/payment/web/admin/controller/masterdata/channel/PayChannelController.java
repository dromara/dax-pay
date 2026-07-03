package cn.daxpay.open.payment.web.admin.controller.masterdata.channel;

import cn.daxpay.open.payment.masterdata.constants.channel.param.PayChannelQuery;
import cn.daxpay.open.payment.masterdata.constants.channel.result.PayChannelResult;
import cn.daxpay.open.payment.masterdata.constants.product.result.PayProductResult;
import cn.daxpay.open.payment.masterdata.constants.channel.service.PayChannelService;
import cn.daxpay.open.payment.masterdata.constants.product.service.PayProductService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 支付通道（管理端，只读）
///
/// 清单来自 `ChannelEnum` + `pay_channel` 表 + 产品策略推断的通道能力。
@PermCode(menuCode = "payment:platform:pay_channel")
@Validated
@Tag(name = "支付通道")
@RestController
@RequestMapping("/admin/payment/pay-channel")
@RequiredArgsConstructor
public class PayChannelController {

    private final PayChannelService payChannelService;
    private final PayProductService payProductService;

    @PermCode(code = "view", nameCn = "支付通道查看", nameEn = "Pay Channel View")
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<PayChannelResult>> page(PageParam pageParam, PayChannelQuery query, String name) {
        return Res.ok(payChannelService.page(pageParam, query, name));
    }

    @PermCode(code = "view", nameCn = "支付通道查看", nameEn = "Pay Channel View")
    @Operation(summary = "根据编码查询详情")
    @GetMapping("/get")
    public Result<PayChannelResult> findByCode(@NotBlank(message = "{validation.field.code.notBlank}") String code) {
        return Res.ok(payChannelService.findByCode(code));
    }

    @PermCode(code = "view", nameCn = "支付通道查看", nameEn = "Pay Channel View")
    @Operation(summary = "按通道编码查询所属支付产品")
    @GetMapping("/list-products")
    public Result<List<PayProductResult>> listProducts(
            @NotBlank(message = "{validation.field.channel.notBlank}") String channel) {
        return Res.ok(payProductService.listByChannel(channel));
    }

    @PermCode(code = "view", nameCn = "支付通道查看", nameEn = "Pay Channel View")
    @Operation(summary = "启用通道下拉列表")
    @GetMapping("/dropdown")
    public Result<List<LabelValue>> dropdown() {
        return Res.ok(payChannelService.dropdown());
    }
}
