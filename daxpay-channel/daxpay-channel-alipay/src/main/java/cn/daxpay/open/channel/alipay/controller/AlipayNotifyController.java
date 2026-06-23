package cn.daxpay.open.channel.alipay.controller;

import cn.daxpay.open.channel.alipay.service.callback.AlipayCallbackService;
import cn.daxpay.open.payment.pay.service.PayCallbackService;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/// # 支付宝异步通知接收 Controller
///
/// 暴露 `/notify/alipay` 供支付宝异步回调。接收原始 form 参数后委托子应用完成 RSA2 验签,
/// 验签通过则由 [PayCallbackService] 完成订单状态更新, 并返回 `success` 给支付宝。
///
/// 鉴权: 支付宝回调不携带平台凭证, 使用 `@IgnoreAuth` 放行, 安全性由 RSA2 验签保障。
@Slf4j
@IgnoreAuth
@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
public class AlipayNotifyController {

    private final AlipayCallbackService alipayCallbackService;
    private final PayCallbackService payCallbackService;

    /// 支付宝支付异步通知
    @PostMapping("/alipay")
    public String alipayNotify(HttpServletRequest request) {
        // 支付宝以 application/x-www-form-urlencoded 提交参数
        Map<String, String> rawParams = parseFormParams(request);
        log.info("接收支付宝异步通知: out_trade_no={}, trade_status={}",
                rawParams.get("out_trade_no"), rawParams.get("trade_status"));

        // 验签 + 填充回调上下文(失败直接回 fail, 让支付宝重试)
        boolean verified = alipayCallbackService.verifyAndFillContext(rawParams);
        if (!verified) {
            return "fail";
        }
        // 统一回调处理(更新订单状态等)
        payCallbackService.payCallback();
        return "success";
    }

    /// 解析 form 表单参数为 Map<String, String>
    private Map<String, String> parseFormParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> {
            if (v != null && v.length > 0) {
                params.put(k, v[0]);
            }
        });
        return params;
    }
}
