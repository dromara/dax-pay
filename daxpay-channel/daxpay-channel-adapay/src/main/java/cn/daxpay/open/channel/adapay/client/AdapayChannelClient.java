package cn.daxpay.open.channel.adapay.client;

import cn.daxpay.open.channel.adapay.client.req.AdapayCloseReq;
import cn.daxpay.open.channel.adapay.client.req.AdapayPayReq;
import cn.daxpay.open.channel.adapay.client.req.AdapayRefundReq;
import cn.daxpay.open.channel.adapay.client.req.AdapayRefundSyncReq;
import cn.daxpay.open.channel.adapay.client.req.AdapaySyncReq;
import cn.daxpay.open.channel.adapay.client.resp.AdapayCloseResp;
import cn.daxpay.open.channel.adapay.client.resp.AdapayPayResp;
import cn.daxpay.open.channel.adapay.client.resp.AdapayRefundResp;
import cn.daxpay.open.channel.adapay.client.resp.AdapayRefundSyncResp;
import cn.daxpay.open.channel.adapay.client.resp.AdapaySyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 汇付天下通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的汇付天下通道接口。
/// baseUrl 由 [cn.daxpay.open.channel.adapay.config.AdapayClientConfig] 从通道配置统一注入(channel-two)。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface AdapayChannelClient {

    /// 支付下单
    @PostExchange("/channel/adapay/pay")
    DaxResult<AdapayPayResp> pay(@RequestBody AdapayPayReq req);

    /// 支付同步(查询汇付订单状态)
    @PostExchange("/channel/adapay/sync")
    DaxResult<AdapaySyncResp> sync(@RequestBody AdapaySyncReq req);

    /// 关闭订单
    @PostExchange("/channel/adapay/close")
    DaxResult<AdapayCloseResp> close(@RequestBody AdapayCloseReq req);

    /// 退款
    @PostExchange("/channel/adapay/refund")
    DaxResult<AdapayRefundResp> refund(@RequestBody AdapayRefundReq req);

    /// 退款同步(查询退款状态)
    @PostExchange("/channel/adapay/refund-sync")
    DaxResult<AdapayRefundSyncResp> refundSync(@RequestBody AdapayRefundSyncReq req);
}
