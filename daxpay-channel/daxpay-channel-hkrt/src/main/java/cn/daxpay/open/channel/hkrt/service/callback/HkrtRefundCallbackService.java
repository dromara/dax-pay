package cn.daxpay.open.channel.hkrt.service.callback;

import cn.daxpay.open.channel.hkrt.client.HkrtChannelClient;
import cn.daxpay.open.channel.hkrt.client.credential.HkrtSdkCredential;
import cn.daxpay.open.channel.hkrt.client.req.HkrtCallbackParseReq;
import cn.daxpay.open.channel.hkrt.client.resp.HkrtCallbackParseResp;
import cn.daxpay.open.channel.hkrt.dao.isv.HkrtIsvKeyConfigManager;
import cn.daxpay.open.channel.hkrt.entity.isv.HkrtIsvKeyConfig;
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

/// # 海科融通退款回调处理服务
///
/// 海科融通退款异步通知 → 主应用接收原始 JSON body → 转发子应用验签与解析 →
/// 记录退款回调结果。
///
/// 验签与字段解析统一由子应用 dax-pay-channel-two 的 HkrtCallbackParseService 完成,
/// 主应用不再自验签。子应用返回抽象态 tradeStatus(SUCCESS/FAIL)。
///
/// TODO 退款单状态更新待接入退款回调框架(平台目前无独立 RefundCallbackService,
///      后续可通过 RefundService 或新增退款回调入口完成状态流转)。本期仅记录日志。
@Slf4j
@Service
@RequiredArgsConstructor
public class HkrtRefundCallbackService {

    private static final String NOTIFY_SUCCESS = "success";
    private static final String NOTIFY_FAIL = "fail";
    /// 退款成功(抽象态, 由子应用统一转换)
    private static final String REFUND_STATUS_SUCCESS = "SUCCESS";

    private final HkrtIsvKeyConfigManager hkrtIsvKeyConfigManager;
    private final HkrtChannelClient hkrtChannelClient;
    private final RefundCallbackService refundCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 退款回调处理
    public String refundHandle(String channelMchNo, HttpServletRequest request) {
        // 1. 提取回调原始数据(JSON body)
        String body = JakartaServletUtil.getBody(request);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);

        // 2. 获取全局服务商 accessKey(只读查询, 不创建记录)
        HkrtIsvKeyConfig keyConfig = hkrtIsvKeyConfigManager.findByProduct(ProductEnum.HKRT_PAY.getCode())
                .orElse(null);
        if (keyConfig == null || keyConfig.getAccessKey() == null) {
            log.error("海科融通退款回调: 服务商密钥未配置, 无法验签");
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("海科融通退款回调: 服务商密钥未配置");
            payCallbackRecordService.saveRefund(ChannelEnum.HKRT_PAY.getCode(), channelMchNo, failData);
            return NOTIFY_FAIL;
        }

        // 3. 转发子应用验签与解析
        HkrtCallbackParseResp resp = parseCallback(body, keyConfig.getAccessKey());
        if (resp == null) {
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("海科融通退款回调验签/解析失败");
            payCallbackRecordService.saveRefund(ChannelEnum.HKRT_PAY.getCode(), channelMchNo, failData);
            return NOTIFY_FAIL;
        }

        // 4. 构建退款回调数据, 交框架更新退款单状态
        RefundCallbackData callbackData = new RefundCallbackData();
        // out_trade_no = 平台退款号(下单时透传), trade_no = 海科退款流水号
        callbackData.setRefundNo(resp.getOutTradeNo());
        callbackData.setOutRefundNo(resp.getTradeNo());
        if (Objects.equals(REFUND_STATUS_SUCCESS, resp.getTradeStatus())) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("海科融通退款状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        notify.put("outTradeNo", resp.getOutTradeNo());
        notify.put("tradeNo", resp.getTradeNo());
        notify.put("tradeStatus", resp.getTradeStatus());
        callbackData.setCallbackData(notify);
        try {
            refundCallbackService.refundCallback(callbackData);
        } catch (Exception e) {
            log.error("海科融通退款回调业务处理失败: refundNo={}", callbackData.getRefundNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.saveRefund(ChannelEnum.HKRT_PAY.getCode(), channelMchNo, callbackData);
            return NOTIFY_FAIL;
        }
        payCallbackRecordService.saveRefund(ChannelEnum.HKRT_PAY.getCode(), channelMchNo, callbackData);
        return NOTIFY_SUCCESS;
    }

    /// 转发子应用验签解析(退款回调)
    private HkrtCallbackParseResp parseCallback(String body, String accessKey) {
        HkrtSdkCredential credential = new HkrtSdkCredential();
        credential.setAccessKey(accessKey);
        HkrtCallbackParseReq req = new HkrtCallbackParseReq();
        req.setCredential(credential);
        req.setRawData(body);
        req.setRefund(true);
        var result = hkrtChannelClient.parseRefundCallback(req);
        if (result.getCode() != 0) {
            log.error("海科融通退款回调: 子应用解析失败: {}", result.getMsg());
            return null;
        }
        HkrtCallbackParseResp resp = result.getData();
        if (resp == null || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("海科融通退款回调验签失败");
            return null;
        }
        return resp;
    }
}
