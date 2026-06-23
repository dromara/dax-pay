package cn.daxpay.open.channel.douyin.client;

import cn.daxpay.open.channel.douyin.dto.DouyinPayReq;
import cn.daxpay.open.channel.douyin.dto.DouyinPayResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 抖音通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-one 的抖音通道接口。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface DouyinChannelClient {

    /// 支付下单
    @PostExchange("/channel/pay")
    DaxResult<DouyinPayResp> pay(@RequestBody DouyinPayReq req);
}
