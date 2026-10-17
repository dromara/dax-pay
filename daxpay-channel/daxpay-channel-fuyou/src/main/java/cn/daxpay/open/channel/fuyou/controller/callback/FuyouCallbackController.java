package cn.daxpay.open.channel.fuyou.controller.callback;

import cn.daxpay.open.channel.fuyou.service.callback.FuyouDirectPayCallbackService;
import cn.daxpay.open.channel.fuyou.service.callback.FuyouH5CallbackService;
import cn.daxpay.open.channel.fuyou.service.callback.FuyouInternetPayCallbackService;
import cn.daxpay.open.channel.fuyou.service.callback.FuyouPayCallbackService;
import cn.daxpay.open.channel.fuyou.service.callback.FuyouRefundCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// # 富友支付回调通知控制器
///
/// 富友异步通知入口(支付/退款), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// - 服务商模式(`/pay`、`/refund`): form-urlencoded 的 `req` 参数(URL编码的XML), 按产品密钥行公钥验签,
///   凭 mchnt_order_no(关联订单号) 反查 PayTrade
/// - 直连模式(`/direct/pay`): JSON 报文体, 按通道商户取商户密钥验签
/// - 互联网直连(`/internet/pay`): JSON 加密信封报文体, 按通道商户取商户私钥解密, 凭 order_id 直查 PayTrade
/// - H5 收银台(`/h5/pay`、`/h5/refund`): JSON 加密信封报文体, 同互联网直连; 退款为同步终态, 通知仅兜底核对
/// - 合作方模式(`/partner/pay`、`/partner/refund`): form `req` 参数, 与服务商同构, 按合作方产品密钥行验签
@Tag(name = "富友支付回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/fuyou")
@RequiredArgsConstructor
@IgnoreAuth
public class FuyouCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final FuyouPayCallbackService fuyouPayCallbackService;
    private final FuyouRefundCallbackService fuyouRefundCallbackService;
    private final FuyouDirectPayCallbackService fuyouDirectPayCallbackService;
    private final FuyouInternetPayCallbackService fuyouInternetPayCallbackService;
    private final FuyouH5CallbackService fuyouH5CallbackService;

    /// 富友支付回调
    ///
    /// 响应格式: 返回 "1" 表示已收到, 其他视为失败(富友会重试)
    @Operation(summary = "富友支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            @RequestParam("req") String req) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return fuyouPayCallbackService.payHandle(channelMchNo, req);
    }

    /// 富友退款回调
    @Operation(summary = "富友退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("channelMchNo") String channelMchNo,
                               @RequestParam("req") String req) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return fuyouRefundCallbackService.refundHandle(channelMchNo, req);
    }

    /// 富友直连支付回调
    ///
    /// 直连回调报文为 JSON(UTF-8), 原始报文体直接透传子应用验签解析(直连无退款结果通知)
    @Operation(summary = "富友直连支付回调")
    @PostMapping("/direct/pay")
    public String directPayNotify(@PathVariable("mchNo") String mchNo,
                                  @PathVariable("channelMchNo") String channelMchNo,
                                  @RequestBody String body) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return fuyouDirectPayCallbackService.payHandle(channelMchNo, body);
    }

    /// 富友互联网直连支付回调
    ///
    /// 回调报文为 JSON 加密信封 `{mchnt_cd, message}`, 原始报文体透传子应用商户私钥解密解析
    @Operation(summary = "富友互联网直连支付回调")
    @PostMapping("/internet/pay")
    public String internetPayNotify(@PathVariable("mchNo") String mchNo,
                                    @PathVariable("channelMchNo") String channelMchNo,
                                    @RequestBody String body) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return fuyouInternetPayCallbackService.payHandle(channelMchNo, body);
    }

    /// 富友 H5 收银台支付回调
    @Operation(summary = "富友 H5 收银台支付回调")
    @PostMapping("/h5/pay")
    public String h5PayNotify(@PathVariable("mchNo") String mchNo,
                              @PathVariable("channelMchNo") String channelMchNo,
                              @RequestBody String body) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return fuyouH5CallbackService.payHandle(channelMchNo, body);
    }

    /// 富友 H5 收银台退款回调
    ///
    /// 退款为同步终态, 通知仅作兜底核对(幂等更新)
    @Operation(summary = "富友 H5 收银台退款回调")
    @PostMapping("/h5/refund")
    public String h5RefundNotify(@PathVariable("mchNo") String mchNo,
                                 @PathVariable("channelMchNo") String channelMchNo,
                                 @RequestBody String body) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return fuyouH5CallbackService.refundHandle(channelMchNo, body);
    }

    /// 富友合作方支付回调
    ///
    /// 报文与服务商模式同构(form `req` 参数, URL 编码 XML), 按合作方产品密钥行公钥验签
    @Operation(summary = "富友合作方支付回调")
    @PostMapping("/partner/pay")
    public String partnerPayNotify(@PathVariable("mchNo") String mchNo,
                                   @PathVariable("channelMchNo") String channelMchNo,
                                   @RequestParam("req") String req) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return fuyouPayCallbackService.payHandle(channelMchNo, req, ProductEnum.FUYOU_PARTNER.getCode());
    }

    /// 富友合作方退款回调
    @Operation(summary = "富友合作方退款回调")
    @PostMapping("/partner/refund")
    public String partnerRefundNotify(@PathVariable("mchNo") String mchNo,
                                      @PathVariable("channelMchNo") String channelMchNo,
                                      @RequestParam("req") String req) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return fuyouRefundCallbackService.refundHandle(channelMchNo, req, ProductEnum.FUYOU_PARTNER.getCode());
    }
}
