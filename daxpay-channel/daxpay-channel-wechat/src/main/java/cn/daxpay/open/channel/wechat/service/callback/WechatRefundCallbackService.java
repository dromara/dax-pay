package cn.daxpay.open.channel.wechat.service.callback;

import cn.daxpay.open.channel.wechat.client.WechatChannelClient;
import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.client.req.WechatCallbackParseReq;
import cn.daxpay.open.channel.wechat.client.resp.WechatCallbackParseResp;
import cn.daxpay.open.channel.wechat.code.WechatCode;
import cn.daxpay.open.channel.wechat.service.direct.WechatDirectConfigAssembler;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/// # 微信退款回调处理服务
///
/// 微信退款异步通知 → 主应用接收 → 组装凭证 → 转发子应用验签 → 日志记录退款结果。
///
/// TODO 退款单状态更新待接入退款回调框架(平台目前无独立 RefundCallbackService)。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatRefundCallbackService {

    private final WechatChannelClient wechatChannelClient;
    private final WechatDirectConfigAssembler wechatDirectConfigAssembler;

    /// 退款回调处理
    public String refundHandle(String mchNo, String channelMchNo, HttpServletRequest request) {
        // 1. 提取回调原始数据
        String body = JakartaServletUtil.getBody(request);
        Map<String, String> headerMap = JakartaServletUtil.getHeaderMap(request);

        // 2. 组装凭证
        WechatSdkCredential credential = wechatDirectConfigAssembler.buildConfig(mchNo, channelMchNo, null);

        // 3. 转发到子应用验签
        WechatCallbackParseReq req = new WechatCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        req.setSerial(this.getHeader(headerMap, WechatCode.HEADER_SERIAL));
        req.setNonce(this.getHeader(headerMap, WechatCode.HEADER_NONCE));
        req.setSignature(this.getHeader(headerMap, WechatCode.HEADER_SIGNATURE));
        req.setTimestamp(this.getHeader(headerMap, WechatCode.HEADER_TIMESTAMP));

        DaxResult<WechatCallbackParseResp> result = wechatChannelClient.parseRefundCallback(req);
        if (result.getCode() != 0 || result.getData() == null || !result.getData().isVerified()) {
            log.error("微信退款回调验签失败: channelMchNo={}", channelMchNo);
            return WechatCode.NOTIFY_FAIL;
        }

        // 4. 记录退款回调结果, TODO 接入退款单状态更新框架
        WechatCallbackParseResp resp = result.getData();
        log.info("微信退款回调: outRefundNo={}, refundId={}, refundStatus={}, amount={}",
                resp.getOutRefundNo(), resp.getRefundId(), resp.getRefundStatus(), resp.getAmount());
        if (Objects.equals(WechatCode.REFUND_STATUS_SUCCESS, resp.getRefundStatus())) {
            // TODO 退款成功, 更新退款单状态(待接入退款回调框架)
            log.info("微信退款成功: outRefundNo={}", resp.getOutRefundNo());
        }
        return WechatCode.NOTIFY_SUCCESS;
    }

    /// 获取 header(大小写兼容)
    private String getHeader(Map<String, String> headerMap, String name) {
        String value = headerMap.get(name);
        return value != null ? value : headerMap.get(name.toLowerCase());
    }
}
