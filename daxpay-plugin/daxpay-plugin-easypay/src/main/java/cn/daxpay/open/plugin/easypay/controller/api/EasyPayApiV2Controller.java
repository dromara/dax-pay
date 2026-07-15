package cn.daxpay.open.plugin.easypay.controller.api;

import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.plugin.easypay.param.api.v1.EasyPayH5PayParam;
import cn.daxpay.open.plugin.easypay.param.api.v2.*;
import cn.daxpay.open.plugin.easypay.result.api.EasyPayOrderStatusResult;
import cn.daxpay.open.plugin.easypay.result.api.EasyPaySubmitInfoResult;
import cn.daxpay.open.plugin.easypay.result.api.v2.*;
import cn.daxpay.open.plugin.easypay.service.api.EasyPayAssistService;
import cn.daxpay.open.plugin.easypay.service.api.v2.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

/// # 易支付 V2 对外接口
///
@IgnoreAuth
@Validated
@Tag(name = "易支付V2接口")
@RestController
@RequestMapping("/epay/api/v2")
@RequiredArgsConstructor
public class EasyPayApiV2Controller {

    private final EasyPayPayV2Service easyPayPayV2Service;
    private final EasyPayQueryV2Service easyPayQueryV2Service;
    private final EasyPayRefundV2Service easyPayRefundV2Service;
    private final EasyPayCloseV2Service easyPayCloseV2Service;
    private final EasyPayAssistService easyPayAssistService;

    @Operation(summary = "页面跳转支付")
    @RequestMapping(value = "/api/pay/submit", method = {RequestMethod.GET, RequestMethod.POST})
    public RedirectView submit(EasyPaySubmitV2Param param) {
        return new RedirectView(easyPayPayV2Service.submit(param));
    }

    @Operation(summary = "统一下单")
    @RequestMapping(value = "/api/pay/create", method = {RequestMethod.GET, RequestMethod.POST})
    public EasyPayCreateV2Result create(EasyPayCreateV2Param param) {
        return easyPayPayV2Service.create(param);
    }

    @Operation(summary = "查询订单")
    @RequestMapping(value = "/api/pay/query", method = {RequestMethod.GET, RequestMethod.POST})
    public EasyPayOrderV2Result query(EasyPayQueryV2Param param) {
        return easyPayQueryV2Service.query(param);
    }

    @Operation(summary = "退款")
    @RequestMapping(value = "/api/pay/refund", method = {RequestMethod.GET, RequestMethod.POST})
    public EasyPayRefundV2Result refund(EasyPayRefundV2Param param) {
        return easyPayRefundV2Service.refund(param);
    }

    @Operation(summary = "退款查询")
    @RequestMapping(value = {"/api/pay/refund_query", "/api/pay/refundquery"}, method = {RequestMethod.GET, RequestMethod.POST})
    public EasyPayRefundOrderV2Result refundQuery(EasyPayRefundQueryV2Param param) {
        return easyPayRefundV2Service.refundQuery(param);
    }

    @Operation(summary = "关单")
    @RequestMapping(value = "/api/pay/close", method = {RequestMethod.GET, RequestMethod.POST})
    public EasyPayCloseV2Result close(EasyPayCloseV2Param param) {
        return easyPayCloseV2Service.close(param);
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

    @Operation(summary = "H5支付(内部)")
    @PostMapping("/h5/pay")
    public Result<NormalPayResult> h5Pay(@RequestBody @Validated EasyPayH5PayParam param) {
        return Res.ok(easyPayPayV2Service.submitPayByH5(param));
    }
}
