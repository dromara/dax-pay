package cn.daxpay.open.channel.kuaiqian.service.callback;

import cn.daxpay.open.channel.kuaiqian.client.KuaiqianChannelClient;
import cn.daxpay.open.channel.kuaiqian.client.credential.KuaiqianSdkCredential;
import cn.daxpay.open.channel.kuaiqian.client.req.KuaiqianCallbackParseReq;
import cn.daxpay.open.channel.kuaiqian.client.resp.KuaiqianCallbackParseResp;
import cn.daxpay.open.channel.kuaiqian.code.KuaiqianCode;
import cn.daxpay.open.channel.kuaiqian.service.direct.KuaiqianDirectConfigAssembler;
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

/// # 快钱支付回调处理服务
///
/// 快钱异步通知(JSON + 数字信封) → 主应用接收 → 按 channelMchNo 组装凭证 →
/// 转发到子应用用商户 pfx 解密 + 平台证书验签 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态,
/// 应答返回子应用生成的 A9005 加密加签报文。
@Slf4j
@Service
@RequiredArgsConstructor
public class KuaiqianPayCallbackService {

    private final KuaiqianChannelClient kuaiqianChannelClient;
    private final KuaiqianDirectConfigAssembler configAssembler;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String mchNo, String channelMchNo, String body) {
        // 1. 组装凭证(解密需商户 pfx + 平台证书)
        KuaiqianSdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 2. 转发到子应用解密验签
        KuaiqianCallbackParseReq req = new KuaiqianCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        DaxResult<KuaiqianCallbackParseResp> result = kuaiqianChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || Objects.isNull(result.getData()) || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("快钱支付回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("快钱支付回调验签失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return KuaiqianCode.NOTIFY_FAIL;
        }

        // 3. 构建回调数据交由框架处理
        KuaiqianCallbackParseResp resp = result.getData();
        CallbackData callbackData = this.buildCallbackData(resp);
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("快钱支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return KuaiqianCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        // 业务处理成功, 返回 A9005 加密加签应答
        return resp.getAckBody();
    }

    /// 构建框架回调数据
    private CallbackData buildCallbackData(KuaiqianCallbackParseResp resp) {
        CallbackData data = new CallbackData();
        // resp.outTradeNo 是支付时传入的 externalRefNumber = 平台 tradeNo
        data.setTradeNo(resp.getOutTradeNo());
        // resp.outOrderNo 是快钱订单控制号 idOrderCtrl
        data.setOutTradeNo(resp.getOutOrderNo());
        // 完成时间(子应用已解析为 OffsetDateTime)
        data.setFinishTime(resp.getFinishTime());
        // 交易状态映射(S 成功 / F,R 失败)
        data.setTradeStatus(this.mapStatus(resp.getTradeStatus()));
        return data;
    }

    /// 快钱交易状态码 → CallbackStatusEnum
    private String mapStatus(String tradeStatus) {
        if (KuaiqianCode.TRADE_STATUS_SUCCESS.equals(tradeStatus)) {
            return CallbackStatusEnum.SUCCESS.getCode();
        }
        if (KuaiqianCode.TRADE_STATUS_FAIL.equals(tradeStatus) || KuaiqianCode.TRADE_STATUS_REJECT.equals(tradeStatus)) {
            return CallbackStatusEnum.FAIL.getCode();
        }
        // 非成功状态直接传原始值
        return tradeStatus;
    }
}
