package cn.daxpay.open.channel.yeepay.service.callback;

import cn.daxpay.open.channel.yeepay.client.YeepayChannelClient;
import cn.daxpay.open.channel.yeepay.client.credential.YeepaySdkCredential;
import cn.daxpay.open.channel.yeepay.client.req.YeepayCallbackParseReq;
import cn.daxpay.open.channel.yeepay.client.resp.YeepayCallbackParseResp;
import cn.daxpay.open.channel.yeepay.code.YeepayCode;
import cn.daxpay.open.channel.yeepay.service.direct.YeepayDirectConfigAssembler;
import cn.daxpay.open.payment.trade.runtime.bo.RefundCallbackData;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.service.callback.RefundCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/// # 易宝退款回调处理服务
///
/// 易宝退款异步通知 → 主应用接收 → 组装凭证 → 转发子应用解密验签 → 记录退款结果。
///
/// TODO 退款单状态更新待接入退款回调框架(平台目前无独立 RefundCallbackService,
///      后续可通过 RefundService 或新增退款回调入口完成状态流转)。
@Slf4j
@Service
@RequiredArgsConstructor
public class YeepayRefundCallbackService {

    private final YeepayChannelClient yeepayChannelClient;
    private final YeepayDirectConfigAssembler configAssembler;
    private final RefundCallbackService refundCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 退款回调处理
    public String refundHandle(String mchNo, String channelMchNo, HttpServletRequest request) {
        // 1. 提取易宝通知参数
        String response = request.getParameter("response");
        String customerIdentification = request.getParameter("customerIdentification");
        Map<String, Object> notify = new HashMap<>();
        notify.put("response", response);
        notify.put("customerIdentification", customerIdentification);

        // 2. 组装凭证
        YeepaySdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 3. 转发到子应用解密验签
        YeepayCallbackParseReq req = new YeepayCallbackParseReq();
        req.setCredential(credential);
        req.setResponse(response);
        req.setCustomerIdentification(customerIdentification);
        DaxResult<YeepayCallbackParseResp> result = yeepayChannelClient.parseRefundCallback(req);
        if (result.getCode() != 0 || result.getData() == null || !result.getData().isVerified()) {
            log.error("易宝退款回调验签失败: channelMchNo={}", channelMchNo);
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("易宝退款回调验签失败");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return YeepayCode.NOTIFY_FAIL;
        }

        // 4. 构建退款回调数据, 交框架更新退款单状态
        YeepayCallbackParseResp resp = result.getData();
        RefundCallbackData callbackData = new RefundCallbackData();
        callbackData.setRefundNo(resp.getOutRefundNo());
        if (Objects.equals(YeepayCode.TRADE_STATUS_SUCCESS, resp.getTradeStatus())) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("易宝退款状态非成功: " + resp.getTradeStatus());
        }
        notify.put("outRefundNo", resp.getOutRefundNo());
        notify.put("tradeStatus", resp.getTradeStatus());
        callbackData.setCallbackData(notify);
        try {
            refundCallbackService.refundCallback(callbackData);
        } catch (Exception e) {
            log.error("易宝退款回调业务处理失败: refundNo={}", callbackData.getRefundNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.saveRefund(channelMchNo, callbackData);
            return YeepayCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.saveRefund(channelMchNo, callbackData);
        return YeepayCode.NOTIFY_SUCCESS;
    }
}
