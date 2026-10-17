package cn.daxpay.open.channel.ysep.client;

import cn.daxpay.open.channel.ysep.client.req.YsepCallbackParseReq;
import cn.daxpay.open.channel.ysep.client.req.YsepCloseReq;
import cn.daxpay.open.channel.ysep.client.req.YsepPayReq;
import cn.daxpay.open.channel.ysep.client.req.YsepRefundReq;
import cn.daxpay.open.channel.ysep.client.req.YsepSyncReq;
import cn.daxpay.open.channel.ysep.client.resp.YsepCallbackParseResp;
import cn.daxpay.open.channel.ysep.client.resp.YsepCloseResp;
import cn.daxpay.open.channel.ysep.client.resp.YsepPayResp;
import cn.daxpay.open.channel.ysep.client.resp.YsepRefundResp;
import cn.daxpay.open.channel.ysep.client.resp.YsepSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 银盛通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的银盛通道接口。
/// 银盛支付(ysep_pay)/银盛e企通(yseqt_pay)两产品共用本客户端(路径前缀 `/channel/ysep`),
/// 请求携带 product 字段决定报文族, 凭证按通道商户维度组装后随请求下发。
/// 凭证随请求 body 下发, 传输层 AES-GCM 加密由平台统一挂载。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface YsepChannelClient {

    /// 支付下单(按 product 分发到开放平台/e企通接口族)
    @PostExchange("/channel/ysep/pay")
    DaxResult<YsepPayResp> pay(@RequestBody YsepPayReq req);

    /// 支付同步(查询银盛订单状态)
    @PostExchange("/channel/ysep/sync")
    DaxResult<YsepSyncResp> sync(@RequestBody YsepSyncReq req);

    /// 关闭订单
    @PostExchange("/channel/ysep/close")
    DaxResult<YsepCloseResp> close(@RequestBody YsepCloseReq req);

    /// 退款
    @PostExchange("/channel/ysep/refund")
    DaxResult<YsepRefundResp> refund(@RequestBody YsepRefundReq req);

    /// 支付回调验签解析(转发子应用, 按报文形态自动分流两族)
    @PostExchange("/channel/ysep/callback/parse-pay")
    DaxResult<YsepCallbackParseResp> parsePayCallback(@RequestBody YsepCallbackParseReq req);
}
