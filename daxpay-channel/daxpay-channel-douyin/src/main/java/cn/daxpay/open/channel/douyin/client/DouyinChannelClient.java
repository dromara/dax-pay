package cn.daxpay.open.channel.douyin.client;

import cn.daxpay.open.channel.douyin.client.req.DouyinCallbackParseReq;
import cn.daxpay.open.channel.douyin.client.req.DouyinCloseReq;
import cn.daxpay.open.channel.douyin.client.req.DouyinPayReq;
import cn.daxpay.open.channel.douyin.client.req.DouyinRefundReq;
import cn.daxpay.open.channel.douyin.client.req.DouyinRefundSyncReq;
import cn.daxpay.open.channel.douyin.client.req.DouyinSyncReq;
import cn.daxpay.open.channel.douyin.client.req.DouyinTransferReq;
import cn.daxpay.open.channel.douyin.client.req.DouyinAllocReq;
import cn.daxpay.open.channel.douyin.client.resp.DouyinCallbackParseResp;
import cn.daxpay.open.channel.douyin.client.resp.DouyinTransferCallbackParseResp;
import cn.daxpay.open.channel.douyin.client.resp.DouyinCloseResp;
import cn.daxpay.open.channel.douyin.client.resp.DouyinPayResp;
import cn.daxpay.open.channel.douyin.client.resp.DouyinRefundResp;
import cn.daxpay.open.channel.douyin.client.resp.DouyinRefundSyncResp;
import cn.daxpay.open.channel.douyin.client.resp.DouyinSyncResp;
import cn.daxpay.open.channel.douyin.client.resp.DouyinTransferResp;
import cn.daxpay.open.channel.douyin.client.resp.DouyinAllocResp;
import cn.daxpay.open.channel.douyin.client.resp.DouyinAllocCallbackParseResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 抖音通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-one 的抖音通道接口。
/// baseUrl 由 [cn.daxpay.open.channel.douyin.config.DouyinClientConfig] 从通道配置统一注入。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface DouyinChannelClient {

    /// 支付下单
    @PostExchange("/channel/douyin/pay")
    DaxResult<DouyinPayResp> pay(@RequestBody DouyinPayReq req);

    /// 关闭订单
    @PostExchange("/channel/douyin/close")
    DaxResult<DouyinCloseResp> close(@RequestBody DouyinCloseReq req);

    /// 退款
    @PostExchange("/channel/douyin/refund")
    DaxResult<DouyinRefundResp> refund(@RequestBody DouyinRefundReq req);

    /// 支付同步(查询订单状态)
    @PostExchange("/channel/douyin/sync")
    DaxResult<DouyinSyncResp> sync(@RequestBody DouyinSyncReq req);

    /// 退款同步(查询退款状态)
    @PostExchange("/channel/douyin/refund-sync")
    DaxResult<DouyinRefundSyncResp> refundSync(@RequestBody DouyinRefundSyncReq req);

    /// 转账(商家转账)
    @PostExchange("/channel/douyin/transfer")
    DaxResult<DouyinTransferResp> transfer(@RequestBody DouyinTransferReq req);

    /// 转账同步(查询转账状态)
    @PostExchange("/channel/douyin/transfer-sync")
    DaxResult<DouyinTransferResp> transferSync(@RequestBody DouyinTransferReq req);

    /// 支付回调验签解析(转发到子应用验签)
    @PostExchange("/channel/douyin/callback/parse-pay")
    DaxResult<DouyinCallbackParseResp> parsePayCallback(@RequestBody DouyinCallbackParseReq req);

    /// 退款回调验签解析(转发到子应用验签)
    @PostExchange("/channel/douyin/callback/parse-refund")
    DaxResult<DouyinCallbackParseResp> parseRefundCallback(@RequestBody DouyinCallbackParseReq req);

    /// 转账回调验签解析(转发到子应用验签)
    @PostExchange("/channel/douyin/callback/parse-transfer")
    DaxResult<DouyinTransferCallbackParseResp> parseTransferCallback(@RequestBody DouyinCallbackParseReq req);

    /// 发起分账(抖音 splitFund)
    @PostExchange("/channel/douyin/alloc")
    DaxResult<DouyinAllocResp> alloc(@RequestBody DouyinAllocReq req);

    /// 分账同步(查询分账状态 querySplitFund)
    @PostExchange("/channel/douyin/alloc-sync")
    DaxResult<DouyinAllocResp> allocSync(@RequestBody DouyinAllocReq req);

    /// 分账回调验签解析(转发到子应用验签)
    @PostExchange("/channel/douyin/callback/parse-alloc")
    DaxResult<DouyinAllocCallbackParseResp> parseAllocCallback(@RequestBody DouyinCallbackParseReq req);
}
