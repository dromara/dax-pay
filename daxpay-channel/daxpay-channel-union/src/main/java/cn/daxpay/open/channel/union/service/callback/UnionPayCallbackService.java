package cn.daxpay.open.channel.union.service.callback;

import cn.daxpay.open.channel.union.client.UnionChannelClient;
import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.client.req.UnionCallbackParseReq;
import cn.daxpay.open.channel.union.client.resp.UnionCallbackParseResp;
import cn.daxpay.open.channel.union.code.UnionCode;
import cn.daxpay.open.channel.union.service.direct.UnionDirectConfigAssembler;
import cn.daxpay.open.channel.union.util.UnionDateUtil;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.payment.trade.runtime.service.callback.PayCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/// # 云闪付支付回调处理服务
///
/// 银联异步通知 → 主应用接收 → 按 channelMchNo 组装凭证 →
/// 转发到子应用验签(RSA2 证书) →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 银联回调为 form-urlencoded 参数, 与银联商务的 JSON body 不同。
/// 银联交易凭证 queryId 作为通道订单号存入 CallbackData, 退款时作为 origQryId。
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayCallbackService {

    private final UnionChannelClient unionChannelClient;
    private final UnionDirectConfigAssembler configAssembler;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String mchNo, String channelMchNo, HttpServletRequest request) {
        // 1. 提取回调参数(银联回调为 form 参数)
        Map<String, String> params = this.extractFormParams(request);
        Map<String, Object> notify = new HashMap<>(params);

        // 2. 组装凭证
        UnionSdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 3. 转发到子应用证书验签
        UnionCallbackParseReq req = new UnionCallbackParseReq();
        req.setCredential(credential);
        req.setParams(params);
        DaxResult<UnionCallbackParseResp> result = unionChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || result.getData() == null || !result.getData().isVerified()) {
            log.error("云闪付支付回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("云闪付支付回调验签失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return UnionCode.NOTIFY_SUCCESS;
        }

        // 4. 构建回调数据交由框架处理
        UnionCallbackParseResp resp = result.getData();
        CallbackData callbackData = this.buildCallbackData(resp);
        notify.put("outTradeNo", resp.getOutTradeNo());
        notify.put("queryId", resp.getQueryId());
        notify.put("tradeStatus", resp.getTradeStatus());
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("云闪付支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return UnionCode.NOTIFY_SUCCESS;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        return UnionCode.NOTIFY_SUCCESS;
    }

    /// 从 HttpServletRequest 提取 form 参数(银联回调为 application/x-www-form-urlencoded)
    private Map<String, String> extractFormParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> {
            if (v != null && v.length > 0) {
                params.put(k, v[0]);
            }
        });
        return params;
    }

    /// 构建框架回调数据
    private CallbackData buildCallbackData(UnionCallbackParseResp resp) {
        CallbackData data = new CallbackData();
        // resp.outTradeNo 是银联 orderId = 平台 tradeNo
        data.setTradeNo(resp.getOutTradeNo());
        // 银联 queryId 作为通道订单号存入(退款时作为 origQryId)
        data.setOutTradeNo(resp.getQueryId());
        data.setFinishTime(UnionDateUtil.parseCst(resp.getFinishTime()));
        data.setTradeStatus(this.mapStatus(resp.getTradeStatus()));
        return data;
    }

    /// 统一状态码 → CallbackStatusEnum
    private String mapStatus(String tradeStatus) {
        if (UnionCode.TRADE_STATUS_SUCCESS.equals(tradeStatus)) {
            return CallbackStatusEnum.SUCCESS.getCode();
        }
        return tradeStatus;
    }
}
