package cn.daxpay.open.channel.duolabao.controller.callback;

import cn.daxpay.open.channel.duolabao.service.callback.DuolabaoPayCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 哆啦宝支付回调通知控制器
///
/// 哆啦宝异步通知入口(支付回调, JSON 报文 + Header timestamp/token), 不走 Sa-Token 认证
/// (由 @IgnoreAuth 注解放行)。URL 中的 channelMchNo 用于回调时组装凭证(验签需 secretKey),
/// Header 的 timestamp/token 为验签参与字段, 与原始报文一起转发子应用验签。
/// 应答为哆啦宝约定文本串 success/error(由子应用生成后原样返回)。
///
/// 哆啦宝仅有支付异步通知(退款为同步接口), 仅提供支付回调端点。
@Tag(name = "哆啦宝支付回调通知控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/duolabao")
@IgnoreAuth
public class DuolabaoCallbackController {

    private final MerchantContextLoader merchantContextLoader;

    private final DuolabaoPayCallbackService duolabaoPayCallbackService;

    /// 哆啦宝支付回调
    @Operation(summary = "哆啦宝支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            @RequestHeader(value = "timestamp", required = false) String timestamp,
                            @RequestHeader(value = "token", required = false) String token,
                            @RequestBody String body) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return duolabaoPayCallbackService.payHandle(mchNo, channelMchNo, body, timestamp, token);
    }
}
