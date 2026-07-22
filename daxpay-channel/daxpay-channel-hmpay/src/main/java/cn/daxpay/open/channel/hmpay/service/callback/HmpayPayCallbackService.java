package cn.daxpay.open.channel.hmpay.service.callback;

import cn.daxpay.open.channel.hmpay.client.HmpayChannelClient;
import cn.daxpay.open.channel.hmpay.client.credential.HmpaySdkCredential;
import cn.daxpay.open.channel.hmpay.client.req.HmpayCallbackParseReq;
import cn.daxpay.open.channel.hmpay.client.resp.HmpayCallbackParseResp;
import cn.daxpay.open.channel.hmpay.dao.isv.HmpayIsvKeyConfigManager;
import cn.daxpay.open.channel.hmpay.entity.isv.HmpayIsvKeyConfig;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.service.callback.PayCallbackService;
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

/// # 河马付支付回调处理服务
///
/// 杉德异步通知 → 主应用接收原始 body → 转发子应用用杉德公钥 RSA 验签 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 主应用不依赖杉德签名实现(SDK 隔离), 验签/解析由子应用 dax-pay-channel-two 承担。
/// 杉德回调成功响应约定为 `success`。
@Slf4j
@Service
@RequiredArgsConstructor
public class HmpayPayCallbackService {

    private static final String NOTIFY_FAIL = "FAIL";
    private static final String NOTIFY_SUCCESS = "success";

    private final HmpayChannelClient hmpayChannelClient;
    private final HmpayIsvKeyConfigManager hmpayIsvKeyConfigManager;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String channelMchNo, HttpServletRequest request) {
        // 1. 提取回调原始报文(杉德回调为 form 表单)
        String body = JakartaServletUtil.getBody(request);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);

        // 2. 获取服务商公钥(用于子应用验签, 只读查询)
        HmpayIsvKeyConfig keyConfig = hmpayIsvKeyConfigManager
                .findByProduct(ProductEnum.HM_PAY.getCode())
                .orElse(null);
        if (keyConfig == null || StrUtil.isBlank(keyConfig.getPublicKey())) {
            log.error("河马付支付回调: 服务商公钥未配置, 无法验签");
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("河马付支付回调: 服务商公钥未配置");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return NOTIFY_FAIL;
        }

        // 3. 组装凭证(只需 publicKey)并转发子应用验签解析
        HmpaySdkCredential credential = new HmpaySdkCredential();
        credential.setPublicKey(keyConfig.getPublicKey());
        HmpayCallbackParseReq req = new HmpayCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);

        DaxResult<HmpayCallbackParseResp> result = hmpayChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || result.getData() == null
                || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("河马付支付回调验签/解析失败");
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("河马付支付回调验签/解析失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return NOTIFY_FAIL;
        }
        HmpayCallbackParseResp resp = result.getData();

        // 4. 构建 CallbackData 并交由框架更新订单
        CallbackData callbackData = new CallbackData();
        // 平台订单号(杉德 out_order_no 回显 = 下单时传入的 tradeNo)
        callbackData.setTradeNo(resp.getOutTradeNo());
        // 杉德流水号
        callbackData.setOutTradeNo(resp.getTradeNo());
        // 杉德仅推送成功通知
        callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        callbackData.setFinishTime(resp.getFinishTime());
        notify.put("outTradeNo", resp.getOutTradeNo());
        notify.put("tradeNo", resp.getTradeNo());
        callbackData.setCallbackData(notify);

        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("河马付支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return NOTIFY_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        // 5. 杉德要求返回 success 表示接收成功
        return NOTIFY_SUCCESS;
    }
}
