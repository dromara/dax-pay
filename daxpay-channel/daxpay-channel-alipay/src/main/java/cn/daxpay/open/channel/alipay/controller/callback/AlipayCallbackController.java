package cn.daxpay.open.channel.alipay.controller.callback;

import cn.daxpay.open.channel.alipay.service.callback.AlipayCallbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 支付宝支付回调通知控制器
///
/// 支付宝异步通知入口(支付/退款共用同一端点, 旧版约定无 /pay 后缀, 见 AlipayPayService.buildNotifyUrl),
/// 不走 Sa-Token 认证(由安全配置放行 `/unipay/callback/**`)。
/// 路径带 mchNo/appId 仅作占位与未来路由, 验签所需公钥/证书凭 out_trade_no 反查订单后组装下发子应用。
@Tag(name = "支付宝支付回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{appId}/alipay")
@RequiredArgsConstructor
public class AlipayCallbackController {

    private final AlipayCallbackService alipayCallbackService;

    /// 支付宝回调(支付/退款统一入口, 由服务层按表单参数区分)
    @Operation(summary = "支付宝支付/退款回调")
    @PostMapping
    public String notify(@PathVariable("mchNo") String mchNo,
                         @PathVariable("appId") String appId,
                         HttpServletRequest request) {
        return alipayCallbackService.handle(request);
    }
}
