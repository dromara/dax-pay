package cn.daxpay.open.channel.vbill.controller.callback;

import cn.daxpay.open.channel.vbill.service.callback.VbillPayCallbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/// # 随行付支付回调通知控制器
///
/// 随行付(天阙科技)异步通知入口(支付), 不走 Sa-Token 认证(由安全配置放行 `/unipay/callback/**`)。
/// 随行付回调验签只需全局服务商公钥(从 VbillIsvKeyConfig 读取), 不需 channelMchNo,
/// 凭 ordNo 反查 PayTrade。
@Tag(name = "随行付支付回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{appId}/vbill")
@RequiredArgsConstructor
public class VbillCallbackController {

    private final VbillPayCallbackService vbillPayCallbackService;

    /// 随行付支付回调
    ///
    /// 响应格式: `{"code":"success","msg":"成功"}` 表示已收到, 其他视为失败(随行付会重试)
    @Operation(summary = "随行付支付回调")
    @PostMapping("/pay")
    public Map<String, String> payNotify(@PathVariable("mchNo") String mchNo,
                                         @PathVariable("appId") String appId,
                                         HttpServletRequest request) {
        return vbillPayCallbackService.payHandle(request);
    }
}
