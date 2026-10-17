package cn.daxpay.open.channel.xingyi.service.callback;

import cn.daxpay.open.channel.xingyi.client.XingyiChannelClient;
import cn.daxpay.open.channel.xingyi.client.credential.XingyiSdkCredential;
import cn.daxpay.open.channel.xingyi.client.req.XingyiCallbackParseReq;
import cn.daxpay.open.channel.xingyi.client.resp.XingyiCallbackParseResp;
import cn.daxpay.open.channel.xingyi.code.XingyiCode;
import cn.daxpay.open.channel.xingyi.service.direct.XingyiDirectConfigAssembler;
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

/// # 星驿付支付回调处理服务
///
/// 星驿付异步通知(JSON 报文, 大写字段) → 主应用接收 → 按 channelMchNo 组装凭证 →
/// 转发到子应用用商户公钥验签解析 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态,
/// 应答返回星驿付约定的 JSON 串(子应用生成, 主应用原样返回)。
///
/// 凭 THREE_ORDER_NO(支付时上送的商户订单号 = 平台交易号)反查 PayTrade;
/// 星驿付仅有支付异步通知(退款为同步接口), 本服务仅处理支付回调。
@Slf4j
@Service
@RequiredArgsConstructor
public class XingyiPayCallbackService {

    private final XingyiChannelClient xingyiChannelClient;

    private final XingyiDirectConfigAssembler configAssembler;

    private final PayCallbackService payCallbackService;

    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String mchNo, String channelMchNo, String body) {
        // 1. 组装凭证(验签需商户公钥)
        XingyiSdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 2. 转发到子应用验签解析
        XingyiCallbackParseReq req = new XingyiCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        DaxResult<XingyiCallbackParseResp> result = xingyiChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || Objects.isNull(result.getData()) || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("星驿付支付回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("星驿付支付回调验签失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return XingyiCode.NOTIFY_FAIL;
        }

        // 3. 构建框架回调数据
        XingyiCallbackParseResp resp = result.getData();
        CallbackData callbackData = this.buildCallbackData(resp);
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("星驿付支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return XingyiCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        // 业务处理成功, 返回星驿付约定 JSON 应答
        return resp.getAckBody();
    }

    /// 构建框架回调数据
    private CallbackData buildCallbackData(XingyiCallbackParseResp resp) {
        CallbackData data = new CallbackData();
        // resp.outTradeNo 是支付时上送的 orderNo = 平台 tradeNo
        data.setTradeNo(resp.getOutTradeNo());
        // resp.tradeNo 是星驿付订单号(ORDER_NO)
        data.setOutTradeNo(resp.getTradeNo());
        // 完成时间(子应用已解析为 OffsetDateTime)
        data.setFinishTime(resp.getFinishTime());
        // 买家标识
        data.setBuyerId(resp.getBuyerId());
        // 交易状态(子应用已标准化: SUCCESS/FAIL)
        data.setTradeStatus(resp.getTradeStatus());
        return data;
    }
}
