package cn.daxpay.open.channel.baofu.controller.callback;

import cn.daxpay.open.channel.baofu.code.BaofuCode;
import cn.daxpay.open.channel.baofu.service.callback.BaofuPayCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 宝付支付回调通知控制器
///
/// 宝付异步通知入口(支付, form 提交 dataContent + signStr), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// URL 中的 channelMchNo 用于回调时组装凭证(验签需宝付平台证书)。
/// 应答为纯文本 OK / FAIL(报文缺失时 NO), 对齐参考实现。
///
/// 宝付退款为同步终态无退款异步通知, 仅支付回调入口。
@Slf4j
@Tag(name = "宝付支付回调通知控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/baofu")
@IgnoreAuth
public class BaofuCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final BaofuPayCallbackService baofuPayCallbackService;

    /// 宝付支付回调
    @Operation(summary = "宝付支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        // 宝付回调为 form 提交: dataContent(业务 JSON 原文) + signStr(对原文的平台证书签名, hex)
        String dataContent = request.getParameter("dataContent");
        String signStr = request.getParameter("signStr");
        if (dataContent == null || dataContent.isBlank()) {
            // 报文缺失, 对齐参考实现返回 NO
            return BaofuCode.NOTIFY_NO;
        }
        return baofuPayCallbackService.payHandle(mchNo, channelMchNo, dataContent, signStr);
    }
}
