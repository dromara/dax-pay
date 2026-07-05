package cn.daxpay.open.channel.lakala.controller.callback;

import cn.daxpay.open.channel.lakala.service.callback.LakalaPayCallbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 拉卡拉支付回调通知控制器
///
/// 拉卡拉异步通知入口(支付), 不走 Sa-Token 认证(由安全配置放行 `/unipay/callback/**`)。
/// 拉卡拉回调验签只需全局服务商公钥(从 LakalaIsvKeyConfig 读取), 不需 channelMchNo,
/// 因此路径不带 channelMchNo(与抖音不同), 凭 out_trade_no 反查 PayTrade。
@Tag(name = "拉卡拉支付回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{appId}/lakala")
@RequiredArgsConstructor
public class LakalaCallbackController {

    private final LakalaPayCallbackService lakalaPayCallbackService;

    /// 拉卡拉支付回调
    @Operation(summary = "拉卡拉支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("appId") String appId,
                            HttpServletRequest request) {
        return lakalaPayCallbackService.payHandle(request);
    }
}
