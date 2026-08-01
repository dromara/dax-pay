package cn.daxpay.open.channel.union.service.callback;

import cn.daxpay.open.channel.union.client.UnionChannelClient;
import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.client.req.UnionCallbackParseReq;
import cn.daxpay.open.channel.union.client.resp.UnionCallbackParseResp;
import cn.daxpay.open.channel.union.code.UnionCode;
import cn.daxpay.open.channel.union.service.direct.UnionDirectConfigAssembler;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.bo.RefundCallbackData;
import cn.daxpay.open.payment.trade.runtime.service.callback.RefundCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/// # 云闪付退款回调处理服务
///
/// 银联退款异步通知 → 主应用接收 → 组装凭证 → 转发子应用证书验签 → 交框架更新退款单状态。
///
/// 银联回调为 form-urlencoded 参数。
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionRefundCallbackService {

    private final UnionChannelClient unionChannelClient;
    private final UnionDirectConfigAssembler configAssembler;
    private final RefundCallbackService refundCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 退款回调处理
    public String refundHandle(String mchNo, String channelMchNo, HttpServletRequest request) {
        // 1. 提取回调参数
        Map<String, String> params = this.extractFormParams(request);
        Map<String, Object> notify = new HashMap<>(params);

        // 2. 组装凭证
        UnionSdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 3. 转发到子应用证书验签
        UnionCallbackParseReq req = new UnionCallbackParseReq();
        req.setCredential(credential);
        req.setParams(params);
        DaxResult<UnionCallbackParseResp> result = unionChannelClient.parseRefundCallback(req);
        if (result.getCode() != 0 || result.getData() == null || !result.getData().isVerified()) {
            log.error("云闪付退款回调验签失败: channelMchNo={}", channelMchNo);
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("云闪付退款回调验签失败");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return UnionCode.NOTIFY_SUCCESS;
        }

        // 4. 构建退款回调数据, 交框架更新退款单状态
        UnionCallbackParseResp resp = result.getData();
        RefundCallbackData callbackData = new RefundCallbackData();
        callbackData.setRefundNo(resp.getOutRefundNo());
        if (Objects.equals(UnionCode.TRADE_STATUS_SUCCESS, resp.getTradeStatus())) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("云闪付退款状态非成功: " + resp.getTradeStatus());
        }
        notify.put("outRefundNo", resp.getOutRefundNo());
        notify.put("tradeStatus", resp.getTradeStatus());
        callbackData.setCallbackData(notify);
        try {
            refundCallbackService.refundCallback(callbackData);
        } catch (Exception e) {
            log.error("云闪付退款回调业务处理失败: refundNo={}", callbackData.getRefundNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.saveRefund(channelMchNo, callbackData);
            return UnionCode.NOTIFY_SUCCESS;
        }
        payCallbackRecordService.saveRefund(channelMchNo, callbackData);
        return UnionCode.NOTIFY_SUCCESS;
    }

    /// 从 HttpServletRequest 提取 form 参数
    private Map<String, String> extractFormParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> {
            if (v != null && v.length > 0) {
                params.put(k, v[0]);
            }
        });
        return params;
    }
}
