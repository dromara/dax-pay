package cn.daxpay.open.channel.yizhifubj.client;

import cn.daxpay.open.channel.yizhifubj.client.req.YizhifubjCallbackParseReq;
import cn.daxpay.open.channel.yizhifubj.client.req.YizhifubjPayReq;
import cn.daxpay.open.channel.yizhifubj.client.req.YizhifubjRefundReq;
import cn.daxpay.open.channel.yizhifubj.client.req.YizhifubjSyncReq;
import cn.daxpay.open.channel.yizhifubj.client.resp.YizhifubjCallbackParseResp;
import cn.daxpay.open.channel.yizhifubj.client.resp.YizhifubjPayResp;
import cn.daxpay.open.channel.yizhifubj.client.resp.YizhifubjRefundResp;
import cn.daxpay.open.channel.yizhifubj.client.resp.YizhifubjSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 一直付(北京)通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的一直付通道接口(路径前缀 `/channel/yizhifubj`)。
/// baseUrl 由 [cn.daxpay.open.channel.yizhifubj.config.YizhifubjClientConfig] 从通道配置统一注入。
///
/// 一直付无主动关单与独立退款查询接口, 不提供 close / refund-sync 端点
/// (关单为本地语义, 退款同步结果即终态、回调兜底)。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface YizhifubjChannelClient {

    /// 支付下单(/onlinePay/order 统一下单)
    @PostExchange("/channel/yizhifubj/pay")
    DaxResult<YizhifubjPayResp> pay(@RequestBody YizhifubjPayReq req);

    /// 支付同步(/onlinePay/query 订单查询)
    @PostExchange("/channel/yizhifubj/sync")
    DaxResult<YizhifubjSyncResp> sync(@RequestBody YizhifubjSyncReq req);

    /// 退款(/onlinePay/refund 同步受理)
    @PostExchange("/channel/yizhifubj/refund")
    DaxResult<YizhifubjRefundResp> refund(@RequestBody YizhifubjRefundReq req);

    /// 支付回调验签解析(转发到子应用)
    @PostExchange("/channel/yizhifubj/callback/parse-pay")
    DaxResult<YizhifubjCallbackParseResp> parsePayCallback(@RequestBody YizhifubjCallbackParseReq req);

    /// 退款回调验签解析(转发到子应用)
    @PostExchange("/channel/yizhifubj/callback/parse-refund")
    DaxResult<YizhifubjCallbackParseResp> parseRefundCallback(@RequestBody YizhifubjCallbackParseReq req);
}
