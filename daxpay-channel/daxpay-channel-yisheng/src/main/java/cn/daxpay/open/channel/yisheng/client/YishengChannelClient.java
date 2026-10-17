package cn.daxpay.open.channel.yisheng.client;

import cn.daxpay.open.channel.yisheng.client.req.YishengCallbackParseReq;
import cn.daxpay.open.channel.yisheng.client.req.YishengPayReq;
import cn.daxpay.open.channel.yisheng.client.req.YishengRefundReq;
import cn.daxpay.open.channel.yisheng.client.req.YishengSyncReq;
import cn.daxpay.open.channel.yisheng.client.resp.YishengCallbackParseResp;
import cn.daxpay.open.channel.yisheng.client.resp.YishengPayResp;
import cn.daxpay.open.channel.yisheng.client.resp.YishengRefundResp;
import cn.daxpay.open.channel.yisheng.client.resp.YishengSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 易生通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的易生通道接口(路径前缀 `/channel/yisheng`)。
/// baseUrl 由 [cn.daxpay.open.channel.yisheng.config.YishengClientConfig] 从通道配置统一注入。
///
/// 易生无主动关单与退款查询接口(退款为同步终态), 不提供 close / refund-sync 端点
/// (关单为本地语义, 退款同步按「通道不支持查询」软失败); 退款无异步通知, 仅提供支付回调解析端点。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface YishengChannelClient {

    /// 支付下单(扫码 / JSAPI)
    @PostExchange("/channel/yisheng/pay")
    DaxResult<YishengPayResp> pay(@RequestBody YishengPayReq req);

    /// 支付同步(easypay.merchant.query 订单查询)
    @PostExchange("/channel/yisheng/sync")
    DaxResult<YishengSyncResp> sync(@RequestBody YishengSyncReq req);

    /// 退款(easypay.merchant.refund)
    @PostExchange("/channel/yisheng/refund")
    DaxResult<YishengRefundResp> refund(@RequestBody YishengRefundReq req);

    /// 支付回调验签解析(转发到子应用)
    @PostExchange("/channel/yisheng/callback/parse-pay")
    DaxResult<YishengCallbackParseResp> parsePayCallback(@RequestBody YishengCallbackParseReq req);
}
