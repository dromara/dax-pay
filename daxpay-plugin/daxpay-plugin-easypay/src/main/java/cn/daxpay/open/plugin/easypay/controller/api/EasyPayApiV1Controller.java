package cn.daxpay.open.plugin.easypay.controller.api;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.plugin.easypay.param.api.v1.EasyPayCreateV1Param;
import cn.daxpay.open.plugin.easypay.param.api.v1.EasyPayQueryV1Param;
import cn.daxpay.open.plugin.easypay.param.api.v1.EasyPaySubmitV1Param;
import cn.daxpay.open.plugin.easypay.result.api.EasyPayOrderStatusResult;
import cn.daxpay.open.plugin.easypay.result.api.EasyPaySubmitInfoResult;
import cn.daxpay.open.plugin.easypay.result.api.v1.EasyPayCreateV1Result;
import cn.daxpay.open.plugin.easypay.result.api.v1.EasyPayOrderV1Result;
import cn.daxpay.open.plugin.easypay.service.api.EasyPayAssistService;
import cn.daxpay.open.plugin.easypay.service.api.v1.EasyPayPayV1Service;
import cn.daxpay.open.plugin.easypay.service.api.v1.EasyPayQueryV1Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

/// # 易支付 V1 对外接口
///
@IgnoreAuth
@Validated
@Tag(name = "易支付V1接口")
@RestController
@RequestMapping("/epay/api/v1")
@RequiredArgsConstructor
public class EasyPayApiV1Controller {

    private final EasyPayPayV1Service easyPayPayV1Service;
    private final EasyPayQueryV1Service easyPayQueryV1Service;
    private final EasyPayAssistService easyPayAssistService;

    @Operation(summary = "页面跳转支付 submit.php")
    @RequestMapping(value = "/submit.php", method = {RequestMethod.GET, RequestMethod.POST})
    public RedirectView submit(EasyPaySubmitV1Param param) {
        return new RedirectView(easyPayPayV1Service.submit(param));
    }

    @Operation(summary = "统一下单 mapi.php")
    @RequestMapping(value = "/mapi.php", method = {RequestMethod.GET, RequestMethod.POST})
    public EasyPayCreateV1Result create(EasyPayCreateV1Param param) {
        return easyPayPayV1Service.create(param);
    }

    @Operation(summary = "订单查询 api.php")
    @RequestMapping(value = "/api.php", method = {RequestMethod.GET, RequestMethod.POST})
    public EasyPayOrderV1Result query(EasyPayQueryV1Param param) {
        return easyPayQueryV1Service.query(param);
    }

    @Operation(summary = "查询订单状态(内部)")
    @GetMapping("/order/status")
    public Result<EasyPayOrderStatusResult> queryOrderStatus(@RequestParam @NotNull Long orderId) {
        return Res.ok(easyPayAssistService.queryOrderStatus(orderId));
    }

    @Operation(summary = "收银台订单信息(内部)")
    @GetMapping("/order/info")
    public Result<EasyPaySubmitInfoResult> orderInfo(@RequestParam @NotNull Long id) {
        return Res.ok(easyPayAssistService.findSubmitInfo(id));
    }
}
