package cn.daxpay.open.channel.umfpay.client;

import cn.daxpay.open.channel.umfpay.client.req.UmfpayCallbackParseReq;
import cn.daxpay.open.channel.umfpay.client.req.UmfpayPayReq;
import cn.daxpay.open.channel.umfpay.client.req.UmfpayRefundReq;
import cn.daxpay.open.channel.umfpay.client.resp.UmfpayCallbackParseResp;
import cn.daxpay.open.channel.umfpay.client.resp.UmfpayPayResp;
import cn.daxpay.open.channel.umfpay.client.resp.UmfpayRefundResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 联动优势通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的联动优势通道接口(路径前缀 `/channel/umfpay`)。
/// baseUrl 由 [cn.daxpay.open.channel.umfpay.config.UmfpayClientConfig] 从通道配置统一注入。
///
/// 联动优势老网关无订单查询与主动关单接口, 不提供 sync / close / refund-sync 端点
/// (支付同步为软失败桩等待回调确认, 关单为本地语义, 退款为同步终态无需查询)。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface UmfpayChannelClient {

    /// 支付下单(扫码族 / 公众号跳转)
    @PostExchange("/channel/umfpay/pay")
    DaxResult<UmfpayPayResp> pay(@RequestBody UmfpayPayReq req);

    /// 退款(mer_refund, 同步终态)
    @PostExchange("/channel/umfpay/refund")
    DaxResult<UmfpayRefundResp> refund(@RequestBody UmfpayRefundReq req);

    /// 支付回调验签解析(转发到子应用)
    @PostExchange("/channel/umfpay/callback/parse-pay")
    DaxResult<UmfpayCallbackParseResp> parsePayCallback(@RequestBody UmfpayCallbackParseReq req);
}
