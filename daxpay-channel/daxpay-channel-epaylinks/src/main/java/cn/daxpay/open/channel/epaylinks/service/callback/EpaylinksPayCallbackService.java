package cn.daxpay.open.channel.epaylinks.service.callback;

import cn.daxpay.open.channel.epaylinks.client.EpaylinksChannelClient;
import cn.daxpay.open.channel.epaylinks.client.credential.EpaylinksSdkCredential;
import cn.daxpay.open.channel.epaylinks.client.req.EpaylinksCallbackParseReq;
import cn.daxpay.open.channel.epaylinks.client.resp.EpaylinksCallbackParseResp;
import cn.daxpay.open.channel.epaylinks.code.EpaylinksCode;
import cn.daxpay.open.channel.epaylinks.service.direct.EpaylinksDirectConfigAssembler;
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

/// # 易票联支付回调处理服务
///
/// 易票联异步通知(JSON body + x-efps-sign 头) → 主应用接收 → 按 channelMchNo 组装凭证 →
/// 转发到子应用用平台公钥证书验签 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态,
/// 应答返回易票联规约 JSON 串(returnCode=0000/0001, 由子应用生成后原样返回)。
///
/// 易票联退款为同步受理语义, 无退款异步通知, 仅本支付回调一种形态。
@Slf4j
@Service
@RequiredArgsConstructor
public class EpaylinksPayCallbackService {

    private final EpaylinksChannelClient epaylinksChannelClient;
    private final EpaylinksDirectConfigAssembler configAssembler;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String mchNo, String channelMchNo, String body, String sign) {
        // 1. 组装凭证(验签需平台公钥证书)
        EpaylinksSdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 2. 转发到子应用验签解析
        EpaylinksCallbackParseReq req = new EpaylinksCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        req.setSign(sign);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        DaxResult<EpaylinksCallbackParseResp> result = epaylinksChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || Objects.isNull(result.getData()) || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("易票联支付回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("易票联支付回调验签失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return EpaylinksCode.NOTIFY_FAIL;
        }

        // 3. 构建回调数据交由框架处理
        EpaylinksCallbackParseResp resp = result.getData();
        CallbackData callbackData = this.buildCallbackData(resp);
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("易票联支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return EpaylinksCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        // 业务处理成功, 返回易票联规约应答 JSON
        return resp.getAckBody();
    }

    /// 构建框架回调数据
    private CallbackData buildCallbackData(EpaylinksCallbackParseResp resp) {
        CallbackData data = new CallbackData();
        // resp.outTradeNo 是支付时传入的 outTradeNo = 平台 tradeNo
        data.setTradeNo(resp.getOutTradeNo());
        // resp.outOrderNo 是易票联平台交易号 transactionNo(退款时需上送)
        data.setOutTradeNo(resp.getOutOrderNo());
        // 完成时间(子应用已解析为 OffsetDateTime)
        data.setFinishTime(resp.getFinishTime());
        // 交易状态映射(00 成功 / 01 失败)
        data.setTradeStatus(this.mapStatus(resp.getTradeStatus()));
        return data;
    }

    /// 易票联交易状态码 → CallbackStatusEnum
    private String mapStatus(String tradeStatus) {
        if (EpaylinksCode.TRADE_STATUS_SUCCESS.equals(tradeStatus)) {
            return CallbackStatusEnum.SUCCESS.getCode();
        }
        if (EpaylinksCode.TRADE_STATUS_FAIL.equals(tradeStatus)) {
            return CallbackStatusEnum.FAIL.getCode();
        }
        // 非成功状态直接传原始值
        return tradeStatus;
    }
}
