package cn.daxpay.open.channel.fubei.controller.callback;

import cn.daxpay.open.channel.fubei.service.callback.FubeiPayCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/// # 付呗支付回调通知控制器
///
/// 付呗异步通知入口(支付, form-urlencoded 全参数 + MD5 签名), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// 应答为纯文本约定: 成功处理 `success` / 处理失败 `fail` / 业务状态非成功 `status=原始状态码`。
///
/// 付呗无退款异步通知(退款为同步受理终态), 仅提供 `/pay` 端点。
@Tag(name = "付呗支付回调通知控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/fubei")
@IgnoreAuth
public class FubeiCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final FubeiPayCallbackService fubeiPayCallbackService;

    /// 付呗支付回调
    @Operation(summary = "付呗支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            @RequestParam Map<String, String> params) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return fubeiPayCallbackService.payHandle(mchNo, channelMchNo, params);
    }
}
