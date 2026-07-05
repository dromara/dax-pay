package cn.daxpay.open.channel.ums.client;

import cn.daxpay.open.channel.ums.client.req.UmsCallbackParseReq;
import cn.daxpay.open.channel.ums.client.req.UmsCloseReq;
import cn.daxpay.open.channel.ums.client.req.UmsPayReq;
import cn.daxpay.open.channel.ums.client.req.UmsRefundReq;
import cn.daxpay.open.channel.ums.client.req.UmsRefundSyncReq;
import cn.daxpay.open.channel.ums.client.req.UmsSyncReq;
import cn.daxpay.open.channel.ums.client.resp.UmsCallbackParseResp;
import cn.daxpay.open.channel.ums.client.resp.UmsCloseResp;
import cn.daxpay.open.channel.ums.client.resp.UmsPayResp;
import cn.daxpay.open.channel.ums.client.resp.UmsRefundResp;
import cn.daxpay.open.channel.ums.client.resp.UmsRefundSyncResp;
import cn.daxpay.open.channel.ums.client.resp.UmsSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 银联商务通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-one 的银联商务通道接口。
/// baseUrl 由 [cn.daxpay.open.channel.ums.config.UmsClientConfig] 从通道配置统一注入。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface UmsChannelClient {

    /// 支付下单
    @PostExchange("/channel/ums/pay")
    DaxResult<UmsPayResp> pay(@RequestBody UmsPayReq req);

    /// 关闭订单
    @PostExchange("/channel/ums/close")
    DaxResult<UmsCloseResp> close(@RequestBody UmsCloseReq req);

    /// 退款
    @PostExchange("/channel/ums/refund")
    DaxResult<UmsRefundResp> refund(@RequestBody UmsRefundReq req);

    /// 支付同步(查询订单状态)
    @PostExchange("/channel/ums/sync")
    DaxResult<UmsSyncResp> sync(@RequestBody UmsSyncReq req);

    /// 退款同步(查询退款状态)
    @PostExchange("/channel/ums/refund-sync")
    DaxResult<UmsRefundSyncResp> refundSync(@RequestBody UmsRefundSyncReq req);

    /// 支付回调验签解析(转发到子应用验签)
    @PostExchange("/channel/ums/callback/parse-pay")
    DaxResult<UmsCallbackParseResp> parsePayCallback(@RequestBody UmsCallbackParseReq req);

    /// 退款回调验签解析(转发到子应用验签)
    @PostExchange("/channel/ums/callback/parse-refund")
    DaxResult<UmsCallbackParseResp> parseRefundCallback(@RequestBody UmsCallbackParseReq req);
}
