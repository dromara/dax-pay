package cn.daxpay.open.channel.duolabao.client;

import cn.daxpay.open.channel.duolabao.client.req.DuolabaoCallbackParseReq;
import cn.daxpay.open.channel.duolabao.client.req.DuolabaoPayReq;
import cn.daxpay.open.channel.duolabao.client.req.DuolabaoRefundReq;
import cn.daxpay.open.channel.duolabao.client.req.DuolabaoSyncReq;
import cn.daxpay.open.channel.duolabao.client.resp.DuolabaoCallbackParseResp;
import cn.daxpay.open.channel.duolabao.client.resp.DuolabaoPayResp;
import cn.daxpay.open.channel.duolabao.client.resp.DuolabaoRefundResp;
import cn.daxpay.open.channel.duolabao.client.resp.DuolabaoSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 哆啦宝通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的哆啦宝通道接口(路径前缀 `/channel/duolabao`)。
/// baseUrl 由 [cn.daxpay.open.channel.duolabao.config.DuolabaoClientConfig] 从通道配置统一注入。
///
/// 哆啦宝无主动关单与退款查询接口, 不提供 close / refund-sync 端点
/// (关单为本地语义, 退款为同步受理即成功); 仅有支付回调解析端点(退款无异步通知)。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface DuolabaoChannelClient {

    /// 支付下单(聚合扫码 / JS 主动支付)
    @PostExchange("/channel/duolabao/pay")
    DaxResult<DuolabaoPayResp> pay(@RequestBody DuolabaoPayReq req);

    /// 支付同步(queryOrderPayDetail 订单查询)
    @PostExchange("/channel/duolabao/sync")
    DaxResult<DuolabaoSyncResp> sync(@RequestBody DuolabaoSyncReq req);

    /// 退款(同步受理即成功)
    @PostExchange("/channel/duolabao/refund")
    DaxResult<DuolabaoRefundResp> refund(@RequestBody DuolabaoRefundReq req);

    /// 支付回调验签解析(转发到子应用)
    @PostExchange("/channel/duolabao/callback/parse-pay")
    DaxResult<DuolabaoCallbackParseResp> parsePayCallback(@RequestBody DuolabaoCallbackParseReq req);
}
