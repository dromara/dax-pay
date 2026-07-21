package cn.daxpay.open.channel.dougong.service.callback;

import cn.daxpay.open.channel.dougong.client.DougongChannelClient;
import cn.daxpay.open.channel.dougong.client.credential.DougongSdkCredential;
import cn.daxpay.open.channel.dougong.client.req.DougongCallbackParseReq;
import cn.daxpay.open.channel.dougong.client.resp.DougongCallbackParseResp;
import cn.daxpay.open.channel.dougong.dao.isv.DougongIsvKeyConfigManager;
import cn.daxpay.open.channel.dougong.entity.isv.DougongIsvKeyConfig;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.service.callback.PayCallbackService;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
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

/// # 斗拱支付回调处理服务
///
/// 汇付异步通知 → 主应用接收原始 body → 转发子应用用汇付公钥 RSA 验签(汇付 RsaUtils 在子应用侧) →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 主应用不依赖汇付 SDK(SDK 隔离), 验签/解析由子应用 dax-pay-channel-two 承担。
/// 汇付回调成功响应约定为 `RECV_ORD_ID_{hfSeqId}`。
@Slf4j
@Service
@RequiredArgsConstructor
public class DougongPayCallbackService {

    private static final String NOTIFY_FAIL = "FAIL";

    private final DougongChannelClient dougongChannelClient;
    private final DougongIsvKeyConfigManager dougongIsvKeyConfigManager;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String channelMchNo, HttpServletRequest request) {
        // 1. 提取回调原始报文
        String body = JakartaServletUtil.getBody(request);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);

        // 2. 获取服务商公钥(用于子应用验签, 只读查询)
        DougongIsvKeyConfig keyConfig = dougongIsvKeyConfigManager
                .findByProduct(ProductEnum.DOUGONG_PAY.getCode())
                .orElse(null);
        if (keyConfig == null || StrUtil.isBlank(keyConfig.getDgPublicKey())) {
            log.error("斗拱支付回调: 服务商公钥未配置, 无法验签");
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("斗拱支付回调: 服务商公钥未配置");
            payCallbackRecordService.savePay(ChannelEnum.HUIFU.getCode(), channelMchNo, failData);
            return NOTIFY_FAIL;
        }

        // 3. 组装凭证(只需 dgPublicKey)并转发子应用验签解析
        DougongSdkCredential credential = new DougongSdkCredential();
        credential.setDgPublicKey(keyConfig.getDgPublicKey());
        DougongCallbackParseReq req = new DougongCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);

        DaxResult<DougongCallbackParseResp> result = dougongChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || result.getData() == null
                || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("斗拱支付回调验签/解析失败");
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("斗拱支付回调验签/解析失败");
            payCallbackRecordService.savePay(ChannelEnum.HUIFU.getCode(), channelMchNo, failData);
            return NOTIFY_FAIL;
        }
        DougongCallbackParseResp resp = result.getData();

        // 4. 构建 CallbackData 并交由框架更新订单
        CallbackData callbackData = new CallbackData();
        // 平台订单号(汇付 req_seq_id 回显 = 下单时传入的 tradeNo)
        callbackData.setTradeNo(resp.getOutTradeNo());
        // 汇付流水号
        callbackData.setOutTradeNo(resp.getTradeNo());
        if ("S".equals(resp.getTradeStatus())) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeStatus(resp.getTradeStatus());
            callbackData.setCallbackErrorMsg(resp.getErrorMsg());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        notify.put("outTradeNo", resp.getOutTradeNo());
        notify.put("tradeNo", resp.getTradeNo());
        notify.put("tradeStatus", resp.getTradeStatus());
        callbackData.setCallbackData(notify);

        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("斗拱支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(ChannelEnum.HUIFU.getCode(), channelMchNo, callbackData);
            return NOTIFY_FAIL;
        }
        payCallbackRecordService.savePay(ChannelEnum.HUIFU.getCode(), channelMchNo, callbackData);
        // 5. 汇付要求返回 RECV_ORD_ID_{hfSeqId} 表示接收成功
        return "RECV_ORD_ID_" + resp.getTradeNo();
    }
}
