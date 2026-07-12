package cn.daxpay.open.channel.wechat.service.callback;

import cn.daxpay.open.channel.wechat.client.WechatChannelClient;
import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.client.req.WechatCallbackParseReq;
import cn.daxpay.open.channel.wechat.client.resp.WechatCallbackParseResp;
import cn.daxpay.open.channel.wechat.code.WechatCode;
import cn.daxpay.open.channel.wechat.service.direct.WechatDirectConfigAssembler;
import cn.daxpay.open.payment.common.callback.CallbackData;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.runtime.service.callback.PayCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;

/// # 微信支付回调处理服务
///
/// 微信异步通知 → 主应用接收原始 header + body → 按 channelMchNo 组装凭证 →
/// 转发到子应用用 NotificationParser 验签解密 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPayCallbackService {

    private final WechatChannelClient wechatChannelClient;
    private final WechatDirectConfigAssembler wechatDirectConfigAssembler;
    private final PayCallbackService payCallbackService;

    /// 支付回调处理
    public String payHandle(String mchNo, String channelMchNo, HttpServletRequest request) {
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

        DaxResult<WechatCallbackParseResp> result = wechatChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || result.getData() == null || !result.getData().isVerified()) {
            log.error("微信支付回调验签失败: channelMchNo={}", channelMchNo);
            return WechatCode.NOTIFY_FAIL;
        }

        // 4. 构建回调数据交由框架处理
        WechatCallbackParseResp resp = result.getData();
        CallbackData callbackData = this.buildCallbackData(resp);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("微信支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            return WechatCode.NOTIFY_FAIL;
        }
        return WechatCode.NOTIFY_SUCCESS;
    }

    /// 构建框架回调数据
    private CallbackData buildCallbackData(WechatCallbackParseResp resp) {
        CallbackData data = new CallbackData();
        data.setTradeNo(resp.getOutTradeNo());
        data.setOutTradeNo(resp.getTransactionId());
        if (StrUtil.isNotBlank(resp.getSuccessTime())) {
            data.setFinishTime(OffsetDateTime.parse(resp.getSuccessTime()));
        }
        // 交易状态映射
        if (Objects.equals(WechatCode.TRADE_STATE_SUCCESS, resp.getTradeState())
                || Objects.equals(WechatCode.TRADE_STATE_REFUND, resp.getTradeState())) {
            data.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            data.setTradeStatus(resp.getTradeState());
            data.setCallbackErrorMsg("微信回调状态非成功: " + resp.getTradeState());
        }
        return data;
    }

    /// 获取 header(大小写兼容)
    private String getHeader(Map<String, String> headerMap, String name) {
        String value = headerMap.get(name);
        return value != null ? value : headerMap.get(name.toLowerCase());
    }
}
