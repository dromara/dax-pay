package cn.daxpay.open.channel.fuyou.client;

import cn.daxpay.open.channel.fuyou.client.req.FuyouInternetCallbackParseReq;
import cn.daxpay.open.channel.fuyou.client.req.FuyouInternetPayReq;
import cn.daxpay.open.channel.fuyou.client.req.FuyouInternetRefundReq;
import cn.daxpay.open.channel.fuyou.client.req.FuyouInternetSyncReq;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouCallbackParseResp;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouPayResp;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouRefundResp;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 富友互联网直连通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的富友互联网直连(hlwnets/aggpcpay, JSON + RSA 加密信封)接口。
/// 与服务商模式([FuyouChannelClient])/直连商户前置([FuyouDirectChannelClient])端点平行隔离,
/// 路径前缀 `/channel/fuyou/internet`。
///
/// 富友互联网接口族无独立关单与退款查询接口(退款为同步终态), 故无 close/refund-sync 方法。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface FuyouInternetChannelClient {

    /// 支付下单
    @PostExchange("/channel/fuyou/internet/pay")
    DaxResult<FuyouPayResp> pay(@RequestBody FuyouInternetPayReq req);

    /// 支付同步(查询富友订单状态)
    @PostExchange("/channel/fuyou/internet/sync")
    DaxResult<FuyouSyncResp> sync(@RequestBody FuyouInternetSyncReq req);

    /// 退款(同步终态, 无独立退款查询接口)
    @PostExchange("/channel/fuyou/internet/refund")
    DaxResult<FuyouRefundResp> refund(@RequestBody FuyouInternetRefundReq req);

    /// 支付回调解析(转发子应用, 商户私钥解密)
    @PostExchange("/channel/fuyou/internet/callback/parse-pay")
    DaxResult<FuyouCallbackParseResp> parsePayCallback(@RequestBody FuyouInternetCallbackParseReq req);
}
