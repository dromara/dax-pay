package cn.daxpay.open.channel.dougong.service.callback;

import cn.daxpay.open.channel.dougong.client.DougongChannelClient;
import cn.daxpay.open.channel.dougong.client.credential.DougongSdkCredential;
import cn.daxpay.open.channel.dougong.client.req.DougongCallbackParseReq;
import cn.daxpay.open.channel.dougong.client.resp.DougongCallbackParseResp;
import cn.daxpay.open.channel.dougong.dao.isv.DougongIsvKeyConfigManager;
import cn.daxpay.open.channel.dougong.entity.isv.DougongIsvKeyConfig;
import cn.daxpay.open.payment.trade.runtime.bo.RefundCallbackData;
import cn.daxpay.open.payment.common.result.DaxResult;
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

/// # 斗拱退款回调处理服务
///
/// 汇付退款异步通知 → 主应用接收原始 body → 转发子应用用汇付公钥 RSA 验签 → 记录退款回调结果。
///
/// 退款单状态流转已接入 [RefundCallbackService], 由框架完成终态守卫与状态更新。
@Slf4j
@Service
@RequiredArgsConstructor
public class DougongRefundCallbackService {

    private static final String NOTIFY_FAIL = "FAIL";
    private static final String REFUND_STATUS_SUCCESS = "S";

    private final DougongChannelClient dougongChannelClient;
    private final DougongIsvKeyConfigManager dougongIsvKeyConfigManager;
    private final RefundCallbackService refundCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 退款回调处理
    public String refundHandle(String channelMchNo, HttpServletRequest request) {
        // 1. 提取回调原始报文
        String body = JakartaServletUtil.getBody(request);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);

        // 2. 获取服务商公钥(用于子应用验签)
        DougongIsvKeyConfig keyConfig = dougongIsvKeyConfigManager
                .findByProduct(ProductEnum.DOUGONG_PAY.getCode())
                .orElse(null);
        if (keyConfig == null || StrUtil.isBlank(keyConfig.getDgPublicKey())) {
            log.error("斗拱退款回调: 服务商公钥未配置, 无法验签");
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("斗拱退款回调: 服务商公钥未配置");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return NOTIFY_FAIL;
        }

        // 3. 转发子应用验签解析
        DougongSdkCredential credential = new DougongSdkCredential();
        credential.setDgPublicKey(keyConfig.getDgPublicKey());
        DougongCallbackParseReq req = new DougongCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);

        DaxResult<DougongCallbackParseResp> result = dougongChannelClient.parseRefundCallback(req);
        if (result.getCode() != 0 || result.getData() == null
                || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("斗拱退款回调验签/解析失败");
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("斗拱退款回调验签/解析失败");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return NOTIFY_FAIL;
        }
        DougongCallbackParseResp resp = result.getData();

        // 4. 构建退款回调数据, 交框架更新退款单状态
        RefundCallbackData callbackData = new RefundCallbackData();
        // out_trade_no = 平台退款号(下单时透传), trade_no = 汇付流水号
        callbackData.setRefundNo(resp.getOutTradeNo());
        callbackData.setOutRefundNo(resp.getTradeNo());
        if (REFUND_STATUS_SUCCESS.equals(resp.getTradeStatus())) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("斗拱退款状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        notify.put("outTradeNo", resp.getOutTradeNo());
        notify.put("tradeNo", resp.getTradeNo());
        notify.put("tradeStatus", resp.getTradeStatus());
        callbackData.setCallbackData(notify);
        try {
            refundCallbackService.refundCallback(callbackData);
        } catch (Exception e) {
            log.error("斗拱退款回调业务处理失败: refundNo={}", callbackData.getRefundNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.saveRefund(channelMchNo, callbackData);
            return NOTIFY_FAIL;
        }
        payCallbackRecordService.saveRefund(channelMchNo, callbackData);
        // 汇付要求返回 RECV_ORD_ID_{hfSeqId} 表示接收成功
        return "RECV_ORD_ID_" + resp.getTradeNo();
    }
}
