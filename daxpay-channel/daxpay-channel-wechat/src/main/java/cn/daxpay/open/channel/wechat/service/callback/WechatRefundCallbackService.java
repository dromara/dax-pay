package cn.daxpay.open.channel.wechat.service.callback;

import cn.daxpay.open.channel.wechat.client.WechatChannelClient;
import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.client.req.WechatCallbackParseReq;
import cn.daxpay.open.channel.wechat.client.resp.WechatCallbackParseResp;
import cn.daxpay.open.channel.wechat.code.WechatCode;
import cn.daxpay.open.channel.wechat.service.direct.WechatDirectConfigAssembler;
import cn.daxpay.open.channel.wechat.service.isv.WechatIsvConfigAssembler;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.bo.RefundCallbackData;
import cn.daxpay.open.payment.trade.runtime.service.callback.RefundCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/// # 微信退款回调处理服务
///
/// 微信退款异步通知 → 主应用接收 → 组装凭证 → 转发子应用验签 → 更新退款单状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatRefundCallbackService {

    private final WechatChannelClient wechatChannelClient;
    private final WechatDirectConfigAssembler wechatDirectConfigAssembler;
    private final WechatIsvConfigAssembler wechatIsvConfigAssembler;
    private final RefundCallbackService refundCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 直连退款回调处理
    public String refundHandle(String mchNo, String channelMchNo, HttpServletRequest request) {
        return this.doRefundHandle(mchNo, channelMchNo, request, false);
    }

    /// 服务商退款回调处理
    public String isvRefundHandle(String mchNo, String channelMchNo, HttpServletRequest request) {
        return this.doRefundHandle(mchNo, channelMchNo, request, true);
    }

    private String doRefundHandle(String mchNo, String channelMchNo, HttpServletRequest request, boolean isv) {
        // 1. 提取回调原始数据
        String body = JakartaServletUtil.getBody(request);
        Map<String, String> headerMap = JakartaServletUtil.getHeaderMap(request);

        // 2. 组装凭证(直连/服务商分发); 回调不解析应用, 只装载密钥与证书并校验通道商户归属
        WechatSdkCredential credential = isv
                ? wechatIsvConfigAssembler.buildCallbackConfig(mchNo, channelMchNo)
                : wechatDirectConfigAssembler.buildCallbackConfig(mchNo, channelMchNo);

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
            log.error("微信退款回调验签失败: channelMchNo={}, isv={}", channelMchNo, isv);
            RefundCallbackData failData = new RefundCallbackData();
            Map<String, Object> notify = new HashMap<>();
            notify.put("body", body);
            notify.put("headers", headerMap);
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("微信退款回调验签失败");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return WechatCode.NOTIFY_FAIL;
        }

        // 4. 构建退款回调数据, 交框架更新退款单状态
        WechatCallbackParseResp resp = result.getData();
        RefundCallbackData callbackData = new RefundCallbackData();
        callbackData.setRefundNo(resp.getOutRefundNo());
        callbackData.setOutRefundNo(resp.getRefundId());
        if (Objects.equals(WechatCode.REFUND_STATUS_SUCCESS, resp.getRefundStatus())) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("微信退款状态非成功: " + resp.getRefundStatus());
        }
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        notify.put("outRefundNo", resp.getOutRefundNo());
        notify.put("refundId", resp.getRefundId());
        notify.put("refundStatus", resp.getRefundStatus());
        callbackData.setCallbackData(notify);
        try {
            refundCallbackService.refundCallback(callbackData);
        } catch (Exception e) {
            log.error("微信退款回调业务处理失败: refundNo={}", callbackData.getRefundNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.saveRefund(channelMchNo, callbackData);
            return WechatCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.saveRefund(channelMchNo, callbackData);
        return WechatCode.NOTIFY_SUCCESS;
    }

    /// 获取 header(大小写兼容)
    private String getHeader(Map<String, String> headerMap, String name) {
        String value = headerMap.get(name);
        return value != null ? value : headerMap.get(name.toLowerCase());
    }
}
