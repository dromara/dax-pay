package cn.daxpay.open.channel.dougong.client;

import cn.daxpay.open.channel.dougong.client.req.DougongCallbackParseReq;
import cn.daxpay.open.channel.dougong.client.req.DougongCloseReq;
import cn.daxpay.open.channel.dougong.client.req.DougongPayReq;
import cn.daxpay.open.channel.dougong.client.req.DougongRefundReq;
import cn.daxpay.open.channel.dougong.client.req.DougongRefundSyncReq;
import cn.daxpay.open.channel.dougong.client.req.DougongSyncReq;
import cn.daxpay.open.channel.dougong.client.resp.DougongCallbackParseResp;
import cn.daxpay.open.channel.dougong.client.resp.DougongCloseResp;
import cn.daxpay.open.channel.dougong.client.resp.DougongPayResp;
import cn.daxpay.open.channel.dougong.client.resp.DougongRefundResp;
import cn.daxpay.open.channel.dougong.client.resp.DougongRefundSyncResp;
import cn.daxpay.open.channel.dougong.client.resp.DougongSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 斗拱通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的斗拱(汇付天下)通道接口。
/// 斗拱为聚合服务商模式(微信/支付宝/银联), 路径前缀 `/channel/dougong`。
/// baseUrl 由 [cn.daxpay.open.channel.dougong.config.DougongClientConfig] 从通道配置统一注入。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface DougongChannelClient {

    /// 支付下单
    @PostExchange("/channel/dougong/pay")
    DaxResult<DougongPayResp> pay(@RequestBody DougongPayReq req);

    /// 关闭订单
    @PostExchange("/channel/dougong/close")
    DaxResult<DougongCloseResp> close(@RequestBody DougongCloseReq req);

    /// 退款
    @PostExchange("/channel/dougong/refund")
    DaxResult<DougongRefundResp> refund(@RequestBody DougongRefundReq req);

    /// 支付同步(查询订单状态)
    @PostExchange("/channel/dougong/sync")
    DaxResult<DougongSyncResp> sync(@RequestBody DougongSyncReq req);

    /// 退款同步(查询退款状态)
    @PostExchange("/channel/dougong/refund-sync")
    DaxResult<DougongRefundSyncResp> refundSync(@RequestBody DougongRefundSyncReq req);

    /// 支付回调验签解析(转发到子应用验签)
    @PostExchange("/channel/dougong/callback/parse-pay")
    DaxResult<DougongCallbackParseResp> parsePayCallback(@RequestBody DougongCallbackParseReq req);

    /// 退款回调验签解析(转发到子应用验签)
    @PostExchange("/channel/dougong/callback/parse-refund")
    DaxResult<DougongCallbackParseResp> parseRefundCallback(@RequestBody DougongCallbackParseReq req);
}
