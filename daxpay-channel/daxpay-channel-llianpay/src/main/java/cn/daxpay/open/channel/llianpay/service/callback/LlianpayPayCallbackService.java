package cn.daxpay.open.channel.llianpay.service.callback;

import cn.daxpay.open.channel.llianpay.client.LlianpayChannelClient;
import cn.daxpay.open.channel.llianpay.client.credential.LlianpaySdkCredential;
import cn.daxpay.open.channel.llianpay.client.req.LlianpayCallbackParseReq;
import cn.daxpay.open.channel.llianpay.client.resp.LlianpayCallbackParseResp;
import cn.daxpay.open.channel.llianpay.code.LlianpayCode;
import cn.daxpay.open.channel.llianpay.service.direct.LlianpayDirectConfigAssembler;
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

/// # 连连支付回调处理服务
///
/// 连连异步通知(JSON body + signature-data 头) → 主应用接收 → 按 channelMchNo 组装凭证 →
/// 转发到子应用用连连平台公钥验签 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态,
/// 应答返回连连规约串(Success/Fail, 由子应用生成后原样返回)。
@Slf4j
@Service
@RequiredArgsConstructor
public class LlianpayPayCallbackService {

    private final LlianpayChannelClient llianpayChannelClient;
    private final LlianpayDirectConfigAssembler configAssembler;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String mchNo, String channelMchNo, String body, String sign) {
        // 1. 组装凭证(连连平台公钥验签)
        LlianpaySdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 2. 转发到子应用验签解析
        LlianpayCallbackParseReq req = new LlianpayCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        req.setSign(sign);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        DaxResult<LlianpayCallbackParseResp> result = llianpayChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || Objects.isNull(result.getData()) || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("连连支付回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("连连支付回调验签失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return LlianpayCode.NOTIFY_FAIL;
        }

        // 3. 构建回调数据交由框架处理
        LlianpayCallbackParseResp resp = result.getData();
        CallbackData callbackData = this.buildCallbackData(resp);
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("连连支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return LlianpayCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        // 业务处理成功, 返回连连规约应答串(Success)
        return resp.getAckBody();
    }

    /// 构建框架回调数据
    private CallbackData buildCallbackData(LlianpayCallbackParseResp resp) {
        CallbackData data = new CallbackData();
        // resp.outTradeNo 是支付时传入的 txn_seqno = 平台 tradeNo
        data.setTradeNo(resp.getOutTradeNo());
        // resp.outOrderNo 是连连平台流水号 platform_txno
        data.setOutTradeNo(resp.getOutOrderNo());
        // 完成时间(子应用已解析为 OffsetDateTime)
        data.setFinishTime(resp.getFinishTime());
        // 交易状态映射(SUCCESS 成功 / FAILURE,CANCEL 失败)
        data.setTradeStatus(this.mapStatus(resp.getTradeStatus()));
        return data;
    }

    /// 连连交易状态码 → CallbackStatusEnum
    private String mapStatus(String tradeStatus) {
        if (LlianpayCode.TRADE_STATUS_SUCCESS.equals(tradeStatus)) {
            return CallbackStatusEnum.SUCCESS.getCode();
        }
        if (LlianpayCode.TRADE_STATUS_FAIL.equals(tradeStatus) || LlianpayCode.TRADE_STATUS_CANCEL.equals(tradeStatus)) {
            return CallbackStatusEnum.FAIL.getCode();
        }
        // 非成功状态直接传原始值
        return tradeStatus;
    }
}
