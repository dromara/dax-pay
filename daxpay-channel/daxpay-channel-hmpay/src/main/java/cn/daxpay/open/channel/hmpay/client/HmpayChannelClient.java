package cn.daxpay.open.channel.hmpay.client;

import cn.daxpay.open.channel.hmpay.client.credential.HmpaySdkCredential;
import cn.daxpay.open.channel.hmpay.client.req.HmpayCallbackParseReq;
import cn.daxpay.open.channel.hmpay.client.req.HmpayCloseReq;
import cn.daxpay.open.channel.hmpay.client.req.HmpayPayReq;
import cn.daxpay.open.channel.hmpay.client.req.HmpayRefundReq;
import cn.daxpay.open.channel.hmpay.client.req.HmpayRefundSyncReq;
import cn.daxpay.open.channel.hmpay.client.req.HmpaySyncReq;
import cn.daxpay.open.channel.hmpay.client.resp.HmpayCallbackParseResp;
import cn.daxpay.open.channel.hmpay.client.resp.HmpayCloseResp;
import cn.daxpay.open.channel.hmpay.client.resp.HmpayPayResp;
import cn.daxpay.open.channel.hmpay.client.resp.HmpayRefundResp;
import cn.daxpay.open.channel.hmpay.client.resp.HmpayRefundSyncResp;
import cn.daxpay.open.channel.hmpay.client.resp.HmpaySyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 河马付通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的河马付(杉德)通道接口。
/// 河马付为杉德旗下聚合支付产品(微信/支付宝/扫码/条码), 路径前缀 `/channel/hmpay`。
/// baseUrl 由 [cn.daxpay.open.channel.hmpay.config.HmpayClientConfig] 从通道配置统一注入。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface HmpayChannelClient {

    /// 支付下单
    @PostExchange("/channel/hmpay/pay")
    DaxResult<HmpayPayResp> pay(@RequestBody HmpayPayReq req);

    /// 关闭订单
    @PostExchange("/channel/hmpay/close")
    DaxResult<HmpayCloseResp> close(@RequestBody HmpayCloseReq req);

    /// 退款
    @PostExchange("/channel/hmpay/refund")
    DaxResult<HmpayRefundResp> refund(@RequestBody HmpayRefundReq req);

    /// 支付同步(查询订单状态)
    @PostExchange("/channel/hmpay/sync")
    DaxResult<HmpaySyncResp> sync(@RequestBody HmpaySyncReq req);

    /// 退款同步(查询退款状态)
    @PostExchange("/channel/hmpay/refund-sync")
    DaxResult<HmpayRefundSyncResp> refundSync(@RequestBody HmpayRefundSyncReq req);

    /// 支付回调验签解析(转发到子应用验签)
    @PostExchange("/channel/hmpay/callback/parse-pay")
    DaxResult<HmpayCallbackParseResp> parsePayCallback(@RequestBody HmpayCallbackParseReq req);

    /// 退款回调验签解析(转发到子应用验签)
    @PostExchange("/channel/hmpay/callback/parse-refund")
    DaxResult<HmpayCallbackParseResp> parseRefundCallback(@RequestBody HmpayCallbackParseReq req);
}
