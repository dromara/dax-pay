package cn.daxpay.open.channel.shouqianba.service.callback;

import cn.daxpay.open.channel.shouqianba.client.ShouqianbaChannelClient;
import cn.daxpay.open.channel.shouqianba.client.credential.ShouqianbaSdkCredential;
import cn.daxpay.open.channel.shouqianba.client.req.ShouqianbaCallbackParseReq;
import cn.daxpay.open.channel.shouqianba.client.resp.ShouqianbaCallbackParseResp;
import cn.daxpay.open.channel.shouqianba.code.ShouqianbaCode;
import cn.daxpay.open.channel.shouqianba.service.direct.ShouqianbaDirectConfigAssembler;
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

/// # 收钱吧支付回调处理服务
///
/// 收钱吧异步通知(JSON 原文 + authorization 签名头) → 主应用接收 → 按 channelMchNo 组装凭证 →
/// 转发到子应用用官方公钥 RSA-SHA256 验签 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态,
/// 应答返回纯文本 success / fail(收钱吧协议约定)。
///
/// 仅承载支付回调(收钱吧退款为同步终态, 无退款回调)。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShouqianbaPayCallbackService {

    private final ShouqianbaChannelClient shouqianbaChannelClient;
    private final ShouqianbaDirectConfigAssembler configAssembler;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String mchNo, String channelMchNo, String authorization, String body) {
        // 1. 组装凭证(日志定位与凭证链路对齐; 验签使用官方固定公钥)
        ShouqianbaSdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 2. 转发到子应用验签解析
        ShouqianbaCallbackParseReq req = new ShouqianbaCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        req.setAuthorization(authorization);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        DaxResult<ShouqianbaCallbackParseResp> result = shouqianbaChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || Objects.isNull(result.getData()) || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("收钱吧支付回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("收钱吧支付回调验签失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return ShouqianbaCode.NOTIFY_FAIL;
        }

        // 3. 构建回调数据交由框架处理
        ShouqianbaCallbackParseResp resp = result.getData();
        CallbackData callbackData = this.buildCallbackData(resp);
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("收钱吧支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return ShouqianbaCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        // 业务处理成功, 返回纯文本应答
        return ShouqianbaCode.NOTIFY_SUCCESS;
    }

    /// 构建框架回调数据
    private CallbackData buildCallbackData(ShouqianbaCallbackParseResp resp) {
        CallbackData data = new CallbackData();
        // resp.outTradeNo 是支付时传入的 client_sn = 平台 tradeNo
        data.setTradeNo(resp.getOutTradeNo());
        // resp.outOrderNo 是收钱吧流水号 sn
        data.setOutTradeNo(resp.getOutOrderNo());
        // 完成时间(子应用已解析为 OffsetDateTime)
        data.setFinishTime(resp.getFinishTime());
        data.setBuyerId(resp.getBuyerId());
        // 交易状态映射(PAID 成功; REFUNDED/PARTIAL_REFUNDED 资金曾入账亦视为成功)
        data.setTradeStatus(this.mapStatus(resp.getTradeStatus()));
        return data;
    }

    /// 收钱吧订单状态 → CallbackStatusEnum
    private String mapStatus(String tradeStatus) {
        if (ShouqianbaCode.ORDER_STATUS_PAID.equals(tradeStatus)
                || ShouqianbaCode.ORDER_STATUS_REFUNDED.equals(tradeStatus)
                || ShouqianbaCode.ORDER_STATUS_PARTIAL_REFUNDED.equals(tradeStatus)) {
            return CallbackStatusEnum.SUCCESS.getCode();
        }
        if (ShouqianbaCode.ORDER_STATUS_FAIL.equals(tradeStatus)
                || ShouqianbaCode.ORDER_STATUS_CANCELED.equals(tradeStatus)) {
            return CallbackStatusEnum.FAIL.getCode();
        }
        // 非成功状态直接传原始值
        return tradeStatus;
    }
}
