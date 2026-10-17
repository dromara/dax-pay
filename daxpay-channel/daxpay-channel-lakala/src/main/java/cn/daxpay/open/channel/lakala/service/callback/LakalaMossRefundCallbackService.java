package cn.daxpay.open.channel.lakala.service.callback;

import cn.daxpay.open.channel.lakala.client.resp.MossCallbackParseResp;
import cn.daxpay.open.channel.lakala.dao.isv.LakalaIsvKeyConfigManager;
import cn.daxpay.open.channel.lakala.entity.isv.LakalaIsvKeyConfig;
import cn.daxpay.open.payment.trade.runtime.bo.RefundCallbackData;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.service.callback.RefundCallbackService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/// # 拉卡拉 MOSS 退款回调处理服务
///
/// MOSS 退款异步通知 → 主应用接收 → 转发子应用验签与解析 →
/// 构建 [RefundCallbackData] 交由 [RefundCallbackService] 更新退款单状态。
///
/// 注意: 参照实现未覆盖退款通知报文(退款终态以同步查询兜底),
/// 退款回调字段提取为同构推断(order_no=退款单号), 以联调实测为准。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaMossRefundCallbackService {

    private final LakalaMossPayCallbackService lakalaMossPayCallbackService;
    private final LakalaIsvKeyConfigManager lakalaIsvKeyConfigManager;
    private final RefundCallbackService refundCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 退款回调处理
    public String refundHandle(String channelMchNo, HttpServletRequest request) {
        String body = JakartaServletUtil.getBody(request);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);

        LakalaIsvKeyConfig keyConfig = lakalaIsvKeyConfigManager.findByProduct(ProductEnum.LAKALA_MOSS.getCode())
                .orElse(null);
        if (Objects.isNull(keyConfig) || StrUtil.hasBlank(keyConfig.getLklAppId(), keyConfig.getPrivateKey())) {
            log.error("拉卡拉MOSS退款回调: 密钥未配置, 无法验签解密");
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("拉卡拉MOSS退款回调: 密钥未配置");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return LakalaMossPayCallbackService.NOTIFY_FAIL_BODY;
        }

        MossCallbackParseResp resp = lakalaMossPayCallbackService.parse(body, true, keyConfig);
        if (Objects.isNull(resp) || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("拉卡拉MOSS退款回调验签失败");
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("拉卡拉MOSS退款回调验签失败");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return Objects.isNull(resp) ? LakalaMossPayCallbackService.NOTIFY_FAIL_BODY
                    : StrUtil.blankToDefault(resp.getResponseBody(), LakalaMossPayCallbackService.NOTIFY_FAIL_BODY);
        }

        RefundCallbackData callbackData = new RefundCallbackData();
        // order_no = 平台退款单号
        callbackData.setRefundNo(resp.getOutTradeNo());
        callbackData.setOutRefundNo(resp.getOutRefundNo());
        if (Objects.equals(resp.getTradeStatus(), "SUCCESS")) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("拉卡拉MOSS退款状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        notify.put("outTradeNo", resp.getOutTradeNo());
        notify.put("outRefundNo", resp.getOutRefundNo());
        notify.put("tradeStatus", resp.getTradeStatus());
        callbackData.setCallbackData(notify);
        try {
            refundCallbackService.refundCallback(callbackData);
        } catch (Exception e) {
            log.error("拉卡拉MOSS退款回调业务处理失败: refundNo={}", callbackData.getRefundNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.saveRefund(channelMchNo, callbackData);
            return LakalaMossPayCallbackService.NOTIFY_FAIL_BODY;
        }
        payCallbackRecordService.saveRefund(channelMchNo, callbackData);
        return StrUtil.blankToDefault(resp.getResponseBody(), LakalaMossPayCallbackService.NOTIFY_FAIL_BODY);
    }
}
