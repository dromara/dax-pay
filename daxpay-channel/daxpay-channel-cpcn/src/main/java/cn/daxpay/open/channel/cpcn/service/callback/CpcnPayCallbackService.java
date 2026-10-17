package cn.daxpay.open.channel.cpcn.service.callback;

import cn.daxpay.open.channel.cpcn.client.CpcnChannelClient;
import cn.daxpay.open.channel.cpcn.client.credential.CpcnSdkCredential;
import cn.daxpay.open.channel.cpcn.client.req.CpcnCallbackParseReq;
import cn.daxpay.open.channel.cpcn.client.resp.CpcnCallbackParseResp;
import cn.daxpay.open.channel.cpcn.code.CpcnCode;
import cn.daxpay.open.channel.cpcn.service.direct.CpcnDirectConfigAssembler;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.service.callback.PayCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/// # 中金支付回调处理服务
///
/// 中金异步通知(form 表单 message + signature, 国密 SM2 签名) → 主应用接收 →
/// 按 channelMchNo 组装凭证 → 转发到子应用用平台证书验签 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态,
/// 应答返回子应用生成的 Base64 XML 应答报文(Head.Code=2000)。
@Slf4j
@Service
@RequiredArgsConstructor
public class CpcnPayCallbackService {

    private final CpcnChannelClient cpcnChannelClient;
    private final CpcnDirectConfigAssembler configAssembler;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String mchNo, String channelMchNo, String message, String signature) {
        // 1. 组装凭证(平台证书验签)
        CpcnSdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 2. 转发到子应用验签解析
        CpcnCallbackParseReq req = new CpcnCallbackParseReq();
        req.setCredential(credential);
        req.setMessage(message);
        req.setSignature(signature);
        Map<String, Object> notify = new HashMap<>();
        notify.put("message", message);
        notify.put("signature", signature);
        DaxResult<CpcnCallbackParseResp> result = cpcnChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || Objects.isNull(result.getData()) || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("中金支付回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("中金支付回调验签失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return CpcnCode.NOTIFY_FAIL;
        }

        // 3. 构建回调数据交由框架处理
        CpcnCallbackParseResp resp = result.getData();
        CallbackData callbackData = this.buildCallbackData(resp);
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("中金支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return CpcnCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        // 业务处理成功, 返回子应用生成的 Base64 XML 应答
        return resp.getAckBody();
    }

    /// 构建框架回调数据
    private CallbackData buildCallbackData(CpcnCallbackParseResp resp) {
        CallbackData data = new CallbackData();
        // resp.outTradeNo 是支付时上送的 TxSN = 平台 tradeNo
        data.setTradeNo(resp.getOutTradeNo());
        // resp.outOrderNo 是中金订单号 OrderNo
        data.setOutTradeNo(resp.getOutOrderNo());
        // 完成时间(子应用已解析为 OffsetDateTime)
        data.setFinishTime(resp.getFinishTime());
        // 交易状态映射(Status: 30 成功 / 40 失败 / 其他处理中)
        data.setTradeStatus(this.mapStatus(resp.getTradeStatus()));
        return data;
    }

    /// 中金交易状态码 → CallbackStatusEnum
    private String mapStatus(String tradeStatus) {
        if (CpcnCode.TRADE_STATUS_SUCCESS.equals(tradeStatus)) {
            return CallbackStatusEnum.SUCCESS.getCode();
        }
        if (CpcnCode.TRADE_STATUS_FAIL.equals(tradeStatus)) {
            return CallbackStatusEnum.FAIL.getCode();
        }
        // 非成功状态直接传原始值
        return tradeStatus;
    }
}
