package cn.daxpay.open.channel.fuyou.client;

import cn.daxpay.open.channel.fuyou.client.req.FuyouH5CallbackParseReq;
import cn.daxpay.open.channel.fuyou.client.req.FuyouH5PayReq;
import cn.daxpay.open.channel.fuyou.client.req.FuyouH5RefundReq;
import cn.daxpay.open.channel.fuyou.client.req.FuyouH5SyncReq;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouCallbackParseResp;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouPayResp;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouRefundResp;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 富友 H5 聚合收银台通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的富友 H5 聚合收银台(aggpcpay token, JSON + RSA 加密信封)接口。
/// 与服务商模式([FuyouChannelClient])/直连商户前置([FuyouDirectChannelClient])端点平行隔离,
/// 路径前缀 `/channel/fuyou/h5`。
///
/// 富友收银台接口族无独立关单与退款查询接口(退款为同步终态), 故无 close/refund-sync 方法;
/// 退款结果异步通知经 parseRefundCallback 解密核对。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface FuyouH5ChannelClient {

    /// 支付下单(返回收银台链接/小程序 scheme)
    @PostExchange("/channel/fuyou/h5/pay")
    DaxResult<FuyouPayResp> pay(@RequestBody FuyouH5PayReq req);

    /// 支付同步(查询富友订单状态)
    @PostExchange("/channel/fuyou/h5/sync")
    DaxResult<FuyouSyncResp> sync(@RequestBody FuyouH5SyncReq req);

    /// 退款(同步终态, 附退款结果异步通知)
    @PostExchange("/channel/fuyou/h5/refund")
    DaxResult<FuyouRefundResp> refund(@RequestBody FuyouH5RefundReq req);

    /// 支付回调解析(转发子应用, 商户私钥解密)
    @PostExchange("/channel/fuyou/h5/callback/parse-pay")
    DaxResult<FuyouCallbackParseResp> parsePayCallback(@RequestBody FuyouH5CallbackParseReq req);

    /// 退款回调解析(转发子应用, 商户私钥解密)
    @PostExchange("/channel/fuyou/h5/callback/parse-refund")
    DaxResult<FuyouCallbackParseResp> parseRefundCallback(@RequestBody FuyouH5CallbackParseReq req);
}
