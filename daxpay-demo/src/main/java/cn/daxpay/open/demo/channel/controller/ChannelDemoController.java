package cn.daxpay.open.demo.channel.controller;

import cn.daxpay.open.channel.alipay.client.AlipayChannelClient;
import cn.daxpay.open.channel.alipay.dto.AlipayPayReq;
import cn.daxpay.open.channel.alipay.dto.AlipayPayResp;
import cn.daxpay.open.demo.channel.param.ChannelDemoPayParam;
import cn.daxpay.open.demo.channel.result.ChannelDemoPayResult;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

/// # 通道连通性 Demo 接口
///
/// 演示主应用通过 @HttpExchange 客户端调用子应用 dax-pay-channel-one 的完整链路。
/// 前端发起请求 → 主应用构建 AlipayPayReq → AlipayChannelClient 发送 HTTP → 子应用接收并处理 → 响应返回。
///
/// 鉴权: URL 前缀 `/demo/**` 已在白名单, 类上叠加 `@IgnoreAuth` 双保险。
@Slf4j
@IgnoreAuth
@Validated
@Tag(name = "通道连通性演示")
@RestController
@RequestMapping("/demo/channel")
@RequiredArgsConstructor
public class ChannelDemoController {

    private static final BigDecimal HUNDRED = new BigDecimal("100");

    private final AlipayChannelClient alipayChannelClient;

    /// 发起 Demo 支付请求到子应用
    ///
    /// 通过 @HttpExchange 声明式客户端将请求转发到 dax-pay-channel-one 的 `/channel/pay` 端点。
    /// config 传空 Map, 子应用进入 Demo 模式返回模拟响应。
    @Operation(summary = "发起通道 Demo 支付")
    @PostMapping("/pay")
    public Result<ChannelDemoPayResult> pay(@Validated @RequestBody ChannelDemoPayParam param) {
        log.info("🚀 Demo: 主应用发起通道支付请求, bizOrderNo={}", param.getBizOrderNo());

        // 构建通道请求
        AlipayPayReq req = new AlipayPayReq();
        req.setChannel("alipay");
        req.setBizOrderNo(param.getBizOrderNo());
        req.setAmount(param.getAmount().multiply(HUNDRED).longValue());
        req.setSubject(param.getSubject());
        req.setMethod(param.getMethod());
        // 空配置 → 子应用进入 Demo 模式
        req.setConfig(Map.of());

        // 调用子应用 (通过 @HttpExchange, OTel 自动透传 traceId)
        var channelResult = alipayChannelClient.pay(req);
        if (channelResult.getCode() != 0) {
            throw new RuntimeException("通道支付失败: " + channelResult.getMsg());
        }

        AlipayPayResp resp = channelResult.getData();

        // 构建返回结果 (包含双端 traceId 用于链路对照)
        ChannelDemoPayResult result = new ChannelDemoPayResult();
        result.setBizOrderNo(resp.getBizOrderNo());
        result.setOutOrderNo(resp.getOutOrderNo());
        result.setPayBody(resp.getPayBody());
        result.setPayBodyType(resp.getPayBodyType());
        result.setMainAppTraceId(MDC.get("traceId"));
        result.setSubAppTraceId(channelResult.getTraceId());

        log.info("✅ Demo: 收到子应用响应, outOrderNo={}, subAppTraceId={}",
                result.getOutOrderNo(), result.getSubAppTraceId());

        return Res.ok(result);
    }
}
