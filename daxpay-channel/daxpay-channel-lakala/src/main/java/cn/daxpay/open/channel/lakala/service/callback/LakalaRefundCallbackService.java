package cn.daxpay.open.channel.lakala.service.callback;

import cn.daxpay.open.channel.lakala.client.resp.LakalaCallbackParseResp;
import cn.daxpay.open.channel.lakala.dao.isv.LakalaIsvKeyConfigManager;
import cn.daxpay.open.channel.lakala.entity.isv.LakalaIsvKeyConfig;
import cn.daxpay.open.payment.trade.runtime.bo.RefundCallbackData;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.service.callback.RefundCallbackService;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/// # 拉卡拉退款回调处理服务
///
/// 拉卡拉退款异步通知 → 主应用接收 → 转发子应用验签与解析 →
/// 构建 [RefundCallbackData] 交由 [RefundCallbackService] 更新退款单状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaRefundCallbackService {

    private static final String NOTIFY_SUCCESS = "success";
    private static final String NOTIFY_FAIL = "fail";

    private final LakalaPayCallbackService lakalaPayCallbackService;
    private final LakalaIsvKeyConfigManager lakalaIsvKeyConfigManager;
    private final RefundCallbackService refundCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 退款回调处理
    public String refundHandle(String channelMchNo, HttpServletRequest request) {
        String body = JakartaServletUtil.getBody(request);
        Map<String, String> headerMap = JakartaServletUtil.getHeaderMap(request);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        notify.put("headers", headerMap);

        LakalaIsvKeyConfig keyConfig = lakalaIsvKeyConfigManager.findByProduct(ProductEnum.LAKALA_PAY.getCode())
                .orElse(null);
        if (keyConfig == null || keyConfig.getPublicKey() == null) {
            log.error("拉卡拉退款回调: 服务商密钥未配置, 无法验签");
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("拉卡拉退款回调: 服务商密钥未配置");
            payCallbackRecordService.saveRefund(ChannelEnum.LAKALA_PAY.getCode(), channelMchNo, failData);
            return NOTIFY_FAIL;
        }

        LakalaCallbackParseResp resp = lakalaPayCallbackService.parse(body, headerMap, keyConfig.getPublicKey(), true);
        if (resp == null || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("拉卡拉退款回调验签失败");
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("拉卡拉退款回调验签失败");
            payCallbackRecordService.saveRefund(ChannelEnum.LAKALA_PAY.getCode(), channelMchNo, failData);
            return NOTIFY_FAIL;
        }

        RefundCallbackData callbackData = new RefundCallbackData();
        // out_refund_no = 平台退款号
        callbackData.setRefundNo(resp.getOutTradeNo());
        callbackData.setOutRefundNo(resp.getOutRefundNo());
        if (Objects.equals(resp.getTradeStatus(), "SUCCESS")) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("拉卡拉退款状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        notify.put("outTradeNo", resp.getOutTradeNo());
        notify.put("outRefundNo", resp.getOutRefundNo());
        notify.put("tradeStatus", resp.getTradeStatus());
        callbackData.setCallbackData(notify);
        try {
            refundCallbackService.refundCallback(callbackData);
        } catch (Exception e) {
            log.error("拉卡拉退款回调业务处理失败: refundNo={}", callbackData.getRefundNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.saveRefund(ChannelEnum.LAKALA_PAY.getCode(), channelMchNo, callbackData);
            return NOTIFY_FAIL;
        }
        payCallbackRecordService.saveRefund(ChannelEnum.LAKALA_PAY.getCode(), channelMchNo, callbackData);
        return NOTIFY_SUCCESS;
    }
}
