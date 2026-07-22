package cn.daxpay.open.channel.ums.service.callback;

import cn.daxpay.open.channel.ums.client.UmsChannelClient;
import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.client.req.UmsCallbackParseReq;
import cn.daxpay.open.channel.ums.client.resp.UmsCallbackParseResp;
import cn.daxpay.open.channel.ums.code.UmsCode;
import cn.daxpay.open.channel.ums.service.direct.UmsDirectConfigAssembler;
import cn.daxpay.open.payment.trade.runtime.bo.RefundCallbackData;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.service.callback.RefundCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/// # 银联商务退款回调处理服务
///
/// 银联商务退款异步通知 → 主应用接收 → 组装凭证 → 转发子应用验签 → 日志记录退款结果。
///
/// TODO 退款单状态更新待接入退款回调框架(平台目前无独立 RefundCallbackService,
///      后续可通过 RefundService 或新增退款回调入口完成状态流转)。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmsRefundCallbackService {

    private final UmsChannelClient umsChannelClient;
    private final UmsDirectConfigAssembler configAssembler;
    private final RefundCallbackService refundCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 退款回调处理
    public String refundHandle(String mchNo, String channelMchNo, HttpServletRequest request) {
        // 1. 提取回调参数
        Map<String, String> params = this.extractParams(request);
        Map<String, Object> notify = new HashMap<>(params);

        // 2. 组装凭证
        UmsSdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 3. 转发到子应用验签
        UmsCallbackParseReq req = new UmsCallbackParseReq();
        req.setCredential(credential);
        req.setParams(params);
        DaxResult<UmsCallbackParseResp> result = umsChannelClient.parseRefundCallback(req);
        if (result.getCode() != 0 || result.getData() == null || !result.getData().isVerified()) {
            log.error("银联商务退款回调验签失败: channelMchNo={}", channelMchNo);
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("银联商务退款回调验签失败");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return UmsCode.NOTIFY_FAIL;
        }

        // 4. 构建退款回调数据, 交框架更新退款单状态
        UmsCallbackParseResp resp = result.getData();
        RefundCallbackData callbackData = new RefundCallbackData();
        callbackData.setRefundNo(resp.getOutRefundNo());
        if (Objects.equals(UmsCode.TRADE_STATUS_SUCCESS, resp.getTradeStatus())) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("银联商务退款状态非成功: " + resp.getTradeStatus());
        }
        notify.put("outRefundNo", resp.getOutRefundNo());
        notify.put("tradeStatus", resp.getTradeStatus());
        callbackData.setCallbackData(notify);
        try {
            refundCallbackService.refundCallback(callbackData);
        } catch (Exception e) {
            log.error("银联商务退款回调业务处理失败: refundNo={}", callbackData.getRefundNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.saveRefund(channelMchNo, callbackData);
            return UmsCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.saveRefund(channelMchNo, callbackData);
        return UmsCode.NOTIFY_SUCCESS;
    }

    /// 从 HttpServletRequest 提取回调参数(与支付回调逻辑一致)
    private Map<String, String> extractParams(HttpServletRequest request) {
        String body = JakartaServletUtil.getBody(request);
        Map<String, String> params = new HashMap<>();
        if (StrUtil.isBlank(body)) {
            return params;
        }
        JSONObject json = JSONUtil.parseObj(body);
        for (String key : json.keySet()) {
            Object value = json.get(key);
            if (value != null) {
                if (value instanceof CharSequence) {
                    params.put(key, value.toString());
                } else {
                    params.put(key, JSONUtil.toJsonStr(value));
                }
            }
        }
        return params;
    }
}
