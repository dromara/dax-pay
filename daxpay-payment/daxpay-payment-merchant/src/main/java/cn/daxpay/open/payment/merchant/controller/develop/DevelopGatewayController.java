package cn.daxpay.open.payment.merchant.controller.develop;

import cn.daxpay.open.payment.merchant.param.develop.DevelopParam;
import cn.daxpay.open.payment.merchant.result.develop.DevelopSignResult;
import cn.daxpay.open.payment.merchant.service.develop.DevelopGatewayService;
import cn.daxpay.open.payment.unipay.param.gateway.GatewayPrePayParam;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// 网关支付开发调试(商户端)
///
/// 仅提供签名辅助, 真实预下单由前端模拟商户请求调用 `/unipay/gateway/pre-pay`。
@PermCode(menuCode = PermCodes.Develop.Gateway.MENU)
@Tag(name = "网关支付开发调试服务(商户端)")
@RestController
@RequestMapping("/mch/develop/gateway")
@RequiredArgsConstructor
public class DevelopGatewayController {

    private final DevelopGatewayService developGatewayService;

    @PermCode(code = PermCodes.Action.SIGN)
    @Operation(summary = "网关预下单参数签名")
    @PostMapping("/sign")
    public Result<DevelopSignResult> sign(@RequestBody DevelopParam<GatewayPrePayParam> param) {
        return Res.ok(developGatewayService.sign(param));
    }
}
