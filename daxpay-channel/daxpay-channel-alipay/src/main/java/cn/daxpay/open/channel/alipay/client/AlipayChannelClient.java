package cn.daxpay.open.channel.alipay.client;

import cn.daxpay.open.channel.alipay.dto.AlipayCallbackVerifyReq;
import cn.daxpay.open.channel.alipay.dto.AlipayCallbackVerifyResp;
import cn.daxpay.open.channel.alipay.dto.AlipayPayReq;
import cn.daxpay.open.channel.alipay.dto.AlipayPayResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 支付宝通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-one 的支付宝通道接口。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface AlipayChannelClient {

    /// 支付下单
    @PostExchange("/channel/pay")
    DaxResult<AlipayPayResp> pay(@RequestBody AlipayPayReq req);

    /// 回调验签
    ///
    /// 主应用收到支付宝异步通知后, 将原始参数转发给子应用完成 RSA2 验签与业务字段解析。
    @PostExchange("/channel/alipay/callback/verify")
    DaxResult<AlipayCallbackVerifyResp> callbackVerify(@RequestBody AlipayCallbackVerifyReq req);
}
