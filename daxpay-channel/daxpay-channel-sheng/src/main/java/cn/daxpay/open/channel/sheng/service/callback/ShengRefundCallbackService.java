package cn.daxpay.open.channel.sheng.service.callback;

import cn.daxpay.open.channel.sheng.client.credential.ShengSdkCredential;
import cn.daxpay.open.channel.sheng.client.resp.ShengCallbackParseResp;
import cn.daxpay.open.payment.trade.runtime.bo.RefundCallbackData;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
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

/// # 盛付通退款回调处理服务
///
/// 盛付通退款异步通知 → 主应用接收 → 转发子应用验签与解析 →
/// 构建 [RefundCallbackData] 交由 [RefundCallbackService] 更新退款单状态。
///
/// 验签凭证与支付回调共用 [ShengPayCallbackService#resolveVerifyCredential] 的按产品分流逻辑。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengRefundCallbackService {

    private static final String NOTIFY_SUCCESS = "success";
    private static final String NOTIFY_FAIL = "fail";

    private final ShengPayCallbackService shengPayCallbackService;
    private final RefundCallbackService refundCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 退款回调处理
    public String refundHandle(String channelMchNo, HttpServletRequest request) {
        String body = JakartaServletUtil.getBody(request);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);

        // 按产品分流解析验签凭证(与支付回调共用分流逻辑, 只读查询)
        ShengSdkCredential credential = shengPayCallbackService.resolveVerifyCredential(channelMchNo).orElse(null);
        if (Objects.isNull(credential) || Objects.isNull(credential.getShengpayPublicKey())) {
            log.error("盛付通退款回调: 通道密钥未配置, 无法验签, channelMchNo={}", channelMchNo);
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("盛付通退款回调: 通道密钥未配置");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return NOTIFY_FAIL;
        }

        ShengCallbackParseResp resp = shengPayCallbackService.parse(body, credential, true);
        if (Objects.isNull(resp) || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("盛付通退款回调验签失败");
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("盛付通退款回调验签失败");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return NOTIFY_FAIL;
        }

        RefundCallbackData callbackData = new RefundCallbackData();
        // outRefundNo = 平台退款关联单号(退款时上送的 refundOrderNo)
        callbackData.setRefundNo(resp.getOutRefundNo());
        // tradeNo = 盛付通退款交易号(通道退款流水 refundId)
        callbackData.setOutRefundNo(resp.getTradeNo());
        if (Objects.equals(resp.getTradeStatus(), "SUCCESS")) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("盛付通退款状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        notify.put("outTradeNo", resp.getOutTradeNo());
        notify.put("outRefundNo", resp.getOutRefundNo());
        notify.put("tradeNo", resp.getTradeNo());
        notify.put("amount", resp.getAmount());
        notify.put("tradeStatus", resp.getTradeStatus());
        callbackData.setCallbackData(notify);
        try {
            refundCallbackService.refundCallback(callbackData);
        } catch (Exception e) {
            log.error("盛付通退款回调业务处理失败: refundNo={}", callbackData.getRefundNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.saveRefund(channelMchNo, callbackData);
            return NOTIFY_FAIL;
        }
        payCallbackRecordService.saveRefund(channelMchNo, callbackData);
        return NOTIFY_SUCCESS;
    }
}
