package cn.daxpay.open.channel.longpay.service.callback;

import cn.daxpay.open.channel.longpay.client.LongpayChannelClient;
import cn.daxpay.open.channel.longpay.client.credential.LongpaySdkCredential;
import cn.daxpay.open.channel.longpay.client.req.LongpayCallbackParseReq;
import cn.daxpay.open.channel.longpay.client.resp.LongpayCallbackParseResp;
import cn.daxpay.open.channel.longpay.code.LongpayCode;
import cn.daxpay.open.channel.longpay.service.direct.LongpayDirectConfigAssembler;
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

/// # 建行龙支付支付回调处理服务
///
/// 建行异步通知(urlencoded 表单) → 主应用接收 → 按 channelMchNo 组装凭证 →
/// 转发到子应用用建行公钥 RSA(MD5withRSA) 验签 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态,
/// 应答返回子应用生成的纯文本 SUCCESS(建行规约), 验签失败回 FAIL。
@Slf4j
@Service
@RequiredArgsConstructor
public class LongpayPayCallbackService {

    private final LongpayChannelClient longpayChannelClient;
    private final LongpayDirectConfigAssembler configAssembler;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String mchNo, String channelMchNo, String body) {
        // 1. 组装凭证(验签需建行公钥)
        LongpaySdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 2. 转发到子应用验签解析
        LongpayCallbackParseReq req = new LongpayCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        DaxResult<LongpayCallbackParseResp> result = longpayChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || Objects.isNull(result.getData()) || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("建行龙支付回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("建行龙支付回调验签失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return LongpayCode.NOTIFY_FAIL;
        }

        // 3. 构建回调数据交由框架处理
        LongpayCallbackParseResp resp = result.getData();
        CallbackData callbackData = this.buildCallbackData(resp);
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("建行龙支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return LongpayCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        // 业务处理成功, 返回建行规约纯文本应答
        return resp.getAckBody();
    }

    /// 构建框架回调数据
    private CallbackData buildCallbackData(LongpayCallbackParseResp resp) {
        CallbackData data = new CallbackData();
        // resp.outTradeNo 是支付时传入的 ORDERID = 平台 tradeNo
        data.setTradeNo(resp.getOutTradeNo());
        // resp.outTransNo 是建行流水号 INSTALLNUM
        data.setOutTradeNo(resp.getOutTransNo());
        // 入账完成时间(子应用已解析为 OffsetDateTime)
        data.setFinishTime(resp.getFinishTime());
        // 交易状态映射(Y 成功 / N 失败)
        data.setTradeStatus(this.mapStatus(resp.getTradeStatus()));
        return data;
    }

    /// 建行通知状态码 → CallbackStatusEnum
    private String mapStatus(String tradeStatus) {
        if (LongpayCode.TRADE_STATUS_SUCCESS.equals(tradeStatus)) {
            return CallbackStatusEnum.SUCCESS.getCode();
        }
        if (LongpayCode.TRADE_STATUS_FAIL.equals(tradeStatus)) {
            return CallbackStatusEnum.FAIL.getCode();
        }
        // 非成功状态直接传原始值
        return tradeStatus;
    }
}
