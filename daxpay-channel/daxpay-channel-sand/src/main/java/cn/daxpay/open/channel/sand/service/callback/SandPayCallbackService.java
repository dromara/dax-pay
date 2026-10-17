package cn.daxpay.open.channel.sand.service.callback;

import cn.daxpay.open.channel.sand.client.SandChannelClient;
import cn.daxpay.open.channel.sand.client.credential.SandSdkCredential;
import cn.daxpay.open.channel.sand.client.req.SandCallbackParseReq;
import cn.daxpay.open.channel.sand.client.resp.SandCallbackParseResp;
import cn.daxpay.open.channel.sand.code.SandCode;
import cn.daxpay.open.channel.sand.service.direct.SandDirectConfigAssembler;
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

/// # 杉德支付回调处理服务
///
/// 杉德异步通知(表单 bizData + sign) → 主应用接收 → 按 channelMchNo 组装凭证 →
/// 转发到子应用用杉德公钥证书验签 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态,
/// 应答按处理结果返回杉德规约纯文本(`respCode=000000` / `respCode=020002`)。
@Slf4j
@Service
@RequiredArgsConstructor
public class SandPayCallbackService {

    private final SandChannelClient sandChannelClient;
    private final SandDirectConfigAssembler configAssembler;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String mchNo, String channelMchNo, String bizData, String sign) {
        // 1. 组装凭证(杉德公钥证书验签)
        SandSdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 2. 转发到子应用验签解析
        SandCallbackParseReq req = new SandCallbackParseReq();
        req.setCredential(credential);
        req.setBizData(bizData);
        req.setSign(sign);
        Map<String, Object> notify = new HashMap<>();
        notify.put("bizData", bizData);
        notify.put("sign", sign);
        DaxResult<SandCallbackParseResp> result = sandChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || Objects.isNull(result.getData()) || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("杉德支付回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("杉德支付回调验签失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return SandCode.NOTIFY_FAIL;
        }

        // 3. 构建回调数据交由框架处理
        SandCallbackParseResp resp = result.getData();
        CallbackData callbackData = this.buildCallbackData(resp);
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("杉德支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return SandCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        // 业务处理成功, 返回杉德规约应答
        return SandCode.NOTIFY_SUCCESS;
    }

    /// 构建框架回调数据
    private CallbackData buildCallbackData(SandCallbackParseResp resp) {
        CallbackData data = new CallbackData();
        // resp.outTradeNo 是支付时传入的 outOrderNo = 平台 tradeNo
        data.setTradeNo(resp.getOutTradeNo());
        // resp.tradeNo 是杉德订单流水号 sandSerialNo
        data.setOutTradeNo(resp.getTradeNo());
        // 完成时间(子应用已解析为 OffsetDateTime)
        data.setFinishTime(resp.getFinishTime());
        // 交易状态映射(success 成功 / fail 失败)
        data.setTradeStatus(this.mapStatus(resp.getTradeStatus()));
        return data;
    }

    /// 杉德订单状态码 → CallbackStatusEnum
    private String mapStatus(String tradeStatus) {
        if (SandCode.ORDER_STATUS_SUCCESS.equals(tradeStatus)) {
            return CallbackStatusEnum.SUCCESS.getCode();
        }
        if (SandCode.ORDER_STATUS_FAIL.equals(tradeStatus)) {
            return CallbackStatusEnum.FAIL.getCode();
        }
        // 非成功状态直接传原始值
        return tradeStatus;
    }
}
