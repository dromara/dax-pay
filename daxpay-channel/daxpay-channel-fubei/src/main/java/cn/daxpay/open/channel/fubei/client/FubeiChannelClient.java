package cn.daxpay.open.channel.fubei.client;

import cn.daxpay.open.channel.fubei.client.req.FubeiCallbackParseReq;
import cn.daxpay.open.channel.fubei.client.req.FubeiPayReq;
import cn.daxpay.open.channel.fubei.client.req.FubeiRefundReq;
import cn.daxpay.open.channel.fubei.client.req.FubeiSyncReq;
import cn.daxpay.open.channel.fubei.client.resp.FubeiCallbackParseResp;
import cn.daxpay.open.channel.fubei.client.resp.FubeiPayResp;
import cn.daxpay.open.channel.fubei.client.resp.FubeiRefundResp;
import cn.daxpay.open.channel.fubei.client.resp.FubeiSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 付呗通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的付呗通道接口(路径前缀 `/channel/fubei`)。
/// baseUrl 由 [cn.daxpay.open.channel.fubei.config.FubeiClientConfig] 从通道配置统一注入。
///
/// 付呗无主动关单与退款查询接口, 不提供 close / refund-sync 端点
/// (关单为本地语义; 退款为同步受理终态, 无需同步查询)。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface FubeiChannelClient {

    /// 支付下单(fbpay.order.create / fbpay.order.wap.create)
    @PostExchange("/channel/fubei/pay")
    DaxResult<FubeiPayResp> pay(@RequestBody FubeiPayReq req);

    /// 支付同步(fbpay.order.query)
    @PostExchange("/channel/fubei/sync")
    DaxResult<FubeiSyncResp> sync(@RequestBody FubeiSyncReq req);

    /// 退款(fbpay.order.refund)
    @PostExchange("/channel/fubei/refund")
    DaxResult<FubeiRefundResp> refund(@RequestBody FubeiRefundReq req);

    /// 支付回调验签解析(转发到子应用)
    @PostExchange("/channel/fubei/callback/parse-pay")
    DaxResult<FubeiCallbackParseResp> parsePayCallback(@RequestBody FubeiCallbackParseReq req);
}
