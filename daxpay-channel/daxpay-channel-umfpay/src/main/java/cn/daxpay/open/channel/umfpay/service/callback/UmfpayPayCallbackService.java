package cn.daxpay.open.channel.umfpay.service.callback;

import cn.daxpay.open.channel.umfpay.client.UmfpayChannelClient;
import cn.daxpay.open.channel.umfpay.client.credential.UmfpaySdkCredential;
import cn.daxpay.open.channel.umfpay.client.req.UmfpayCallbackParseReq;
import cn.daxpay.open.channel.umfpay.client.resp.UmfpayCallbackParseResp;
import cn.daxpay.open.channel.umfpay.code.UmfpayCode;
import cn.daxpay.open.channel.umfpay.service.direct.UmfpayDirectConfigAssembler;
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

/// # 联动优势支付回调处理服务
///
/// 联动优势异步通知(GET query 参数 + RSA 签名) → 主应用接收 → 按 channelMchNo 组装凭证 →
/// 转发到子应用用平台公钥验签 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态,
/// 应答返回子应用生成的 HTML META 应答页(text/html)。
///
/// 联动优势无退款异步通知(mer_refund 同步终态), 不存在退款回调场景。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmfpayPayCallbackService {

    private final UmfpayChannelClient umfpayChannelClient;
    private final UmfpayDirectConfigAssembler configAssembler;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String mchNo, String channelMchNo, Map<String, String> params) {
        // 1. 组装凭证(平台公钥验签)
        UmfpaySdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 2. 转发到子应用验签解析
        UmfpayCallbackParseReq req = new UmfpayCallbackParseReq();
        req.setCredential(credential);
        req.setParams(params);
        Map<String, Object> notify = new HashMap<>();
        notify.put("params", params);
        DaxResult<UmfpayCallbackParseResp> result = umfpayChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || Objects.isNull(result.getData()) || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("联动优势支付回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("联动优势支付回调验签失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return UmfpayCode.NOTIFY_FAIL;
        }

        // 3. 构建回调数据交由框架处理
        UmfpayCallbackParseResp resp = result.getData();
        CallbackData callbackData = this.buildCallbackData(resp);
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("联动优势支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return UmfpayCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        // 业务处理成功, 返回子应用生成的 HTML META 应答页
        return resp.getAckBody();
    }

    /// 构建框架回调数据
    private CallbackData buildCallbackData(UmfpayCallbackParseResp resp) {
        CallbackData data = new CallbackData();
        // resp.outTradeNo 是支付时上送的 order_id = 平台 tradeNo
        data.setTradeNo(resp.getOutTradeNo());
        // resp.outTransNo 是联动优势交易号 trade_no(通道外部单号)
        data.setOutTradeNo(resp.getOutTransNo());
        // 交易状态映射(TRADE_SUCCESS 成功, 其余传原始值)
        data.setTradeStatus(this.mapStatus(resp.getTradeStatus()));
        return data;
    }

    /// 联动优势交易状态码 → CallbackStatusEnum
    private String mapStatus(String tradeStatus) {
        if (UmfpayCode.TRADE_STATUS_SUCCESS.equals(tradeStatus)) {
            return CallbackStatusEnum.SUCCESS.getCode();
        }
        // 非成功状态直接传原始值
        return tradeStatus;
    }
}
