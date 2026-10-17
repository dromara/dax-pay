package cn.daxpay.open.channel.baofu.client;

import cn.daxpay.open.channel.baofu.client.req.BaofuCallbackParseReq;
import cn.daxpay.open.channel.baofu.client.req.BaofuPayReq;
import cn.daxpay.open.channel.baofu.client.req.BaofuRefundReq;
import cn.daxpay.open.channel.baofu.client.req.BaofuSyncReq;
import cn.daxpay.open.channel.baofu.client.resp.BaofuCallbackParseResp;
import cn.daxpay.open.channel.baofu.client.resp.BaofuPayResp;
import cn.daxpay.open.channel.baofu.client.resp.BaofuRefundResp;
import cn.daxpay.open.channel.baofu.client.resp.BaofuSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 宝付通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的宝付通道接口(路径前缀 `/channel/baofu`)。
/// baseUrl 由 [cn.daxpay.open.channel.baofu.config.BaofuClientConfig] 从通道配置统一注入。
///
/// 宝付聚合线无主动关单与退款查询接口, 不提供 close / refund-sync 端点
/// (关单为本地语义, 退款为同步终态无需查询);
/// 退款为同步终态无退款异步通知, 仅支付回调解析端点。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface BaofuChannelClient {

    /// 支付下单
    @PostExchange("/channel/baofu/pay")
    DaxResult<BaofuPayResp> pay(@RequestBody BaofuPayReq req);

    /// 支付同步(order_query 订单查询)
    @PostExchange("/channel/baofu/sync")
    DaxResult<BaofuSyncResp> sync(@RequestBody BaofuSyncReq req);

    /// 退款(order_refund, 同步终态)
    @PostExchange("/channel/baofu/refund")
    DaxResult<BaofuRefundResp> refund(@RequestBody BaofuRefundReq req);

    /// 支付回调验签解析(转发到子应用)
    @PostExchange("/channel/baofu/callback/parse-pay")
    DaxResult<BaofuCallbackParseResp> parsePayCallback(@RequestBody BaofuCallbackParseReq req);
}
