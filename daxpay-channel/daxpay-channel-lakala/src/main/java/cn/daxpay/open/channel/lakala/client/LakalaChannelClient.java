package cn.daxpay.open.channel.lakala.client;

import cn.daxpay.open.channel.lakala.client.req.LakalaCallbackParseReq;
import cn.daxpay.open.channel.lakala.client.req.LakalaCloseReq;
import cn.daxpay.open.channel.lakala.client.req.LakalaPayReq;
import cn.daxpay.open.channel.lakala.client.req.LakalaRefundReq;
import cn.daxpay.open.channel.lakala.client.req.LakalaRefundSyncReq;
import cn.daxpay.open.channel.lakala.client.req.LakalaSyncReq;
import cn.daxpay.open.channel.lakala.client.req.MossCallbackParseReq;
import cn.daxpay.open.channel.lakala.client.req.MossCloseReq;
import cn.daxpay.open.channel.lakala.client.req.MossPayReq;
import cn.daxpay.open.channel.lakala.client.req.MossRefundReq;
import cn.daxpay.open.channel.lakala.client.req.MossRefundSyncReq;
import cn.daxpay.open.channel.lakala.client.req.MossSyncReq;
import cn.daxpay.open.channel.lakala.client.resp.LakalaCallbackParseResp;
import cn.daxpay.open.channel.lakala.client.resp.LakalaCloseResp;
import cn.daxpay.open.channel.lakala.client.resp.LakalaPayResp;
import cn.daxpay.open.channel.lakala.client.resp.LakalaRefundResp;
import cn.daxpay.open.channel.lakala.client.resp.LakalaRefundSyncResp;
import cn.daxpay.open.channel.lakala.client.resp.LakalaSyncResp;
import cn.daxpay.open.channel.lakala.client.resp.MossCallbackParseResp;
import cn.daxpay.open.channel.lakala.client.resp.MossCloseResp;
import cn.daxpay.open.channel.lakala.client.resp.MossPayResp;
import cn.daxpay.open.channel.lakala.client.resp.MossRefundResp;
import cn.daxpay.open.channel.lakala.client.resp.MossRefundSyncResp;
import cn.daxpay.open.channel.lakala.client.resp.MossSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 拉卡拉通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的拉卡拉通道接口。
/// 拉卡拉为聚合服务商模式, 不区分直连/服务商, 路径前缀 `/channel/lakala`。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface LakalaChannelClient {

    /// 支付下单
    @PostExchange("/channel/lakala/pay")
    DaxResult<LakalaPayResp> pay(@RequestBody LakalaPayReq req);

    /// 支付同步(查询拉卡拉订单状态)
    @PostExchange("/channel/lakala/sync")
    DaxResult<LakalaSyncResp> sync(@RequestBody LakalaSyncReq req);

    /// 关闭订单
    @PostExchange("/channel/lakala/close")
    DaxResult<LakalaCloseResp> close(@RequestBody LakalaCloseReq req);

    /// 退款
    @PostExchange("/channel/lakala/refund")
    DaxResult<LakalaRefundResp> refund(@RequestBody LakalaRefundReq req);

    /// 退款同步(查询退款状态)
    @PostExchange("/channel/lakala/refund-sync")
    DaxResult<LakalaRefundSyncResp> refundSync(@RequestBody LakalaRefundSyncReq req);

    /// 支付回调验签解析(转发子应用)
    @PostExchange("/channel/lakala/callback/parse-pay")
    DaxResult<LakalaCallbackParseResp> parsePayCallback(@RequestBody LakalaCallbackParseReq req);

    /// 退款回调验签解析(转发子应用)
    @PostExchange("/channel/lakala/callback/parse-refund")
    DaxResult<LakalaCallbackParseResp> parseRefundCallback(@RequestBody LakalaCallbackParseReq req);

    // ===== 拉卡拉 MOSS 产品线(lakala_moss, 开放平台接口体系, 独立路径前缀 /channel/lakala/moss) =====

    /// MOSS 支付下单
    @PostExchange("/channel/lakala/moss/pay")
    DaxResult<MossPayResp> mossPay(@RequestBody MossPayReq req);

    /// MOSS 支付同步(查询订单状态)
    @PostExchange("/channel/lakala/moss/sync")
    DaxResult<MossSyncResp> mossSync(@RequestBody MossSyncReq req);

    /// MOSS 关闭订单(查单确认式)
    @PostExchange("/channel/lakala/moss/close")
    DaxResult<MossCloseResp> mossClose(@RequestBody MossCloseReq req);

    /// MOSS 退款
    @PostExchange("/channel/lakala/moss/refund")
    DaxResult<MossRefundResp> mossRefund(@RequestBody MossRefundReq req);

    /// MOSS 退款同步(查询退款状态)
    @PostExchange("/channel/lakala/moss/refund-sync")
    DaxResult<MossRefundSyncResp> mossRefundSync(@RequestBody MossRefundSyncReq req);

    /// MOSS 支付回调验签解析(转发子应用)
    @PostExchange("/channel/lakala/moss/callback/parse-pay")
    DaxResult<MossCallbackParseResp> mossParsePayCallback(@RequestBody MossCallbackParseReq req);

    /// MOSS 退款回调验签解析(转发子应用)
    @PostExchange("/channel/lakala/moss/callback/parse-refund")
    DaxResult<MossCallbackParseResp> mossParseRefundCallback(@RequestBody MossCallbackParseReq req);
}
