package cn.daxpay.open.channel.duolabao.service.callback;

import cn.daxpay.open.channel.duolabao.client.DuolabaoChannelClient;
import cn.daxpay.open.channel.duolabao.client.credential.DuolabaoSdkCredential;
import cn.daxpay.open.channel.duolabao.client.req.DuolabaoCallbackParseReq;
import cn.daxpay.open.channel.duolabao.client.resp.DuolabaoCallbackParseResp;
import cn.daxpay.open.channel.duolabao.code.DuolabaoCode;
import cn.daxpay.open.channel.duolabao.service.direct.DuolabaoDirectConfigAssembler;
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

/// # 哆啦宝支付回调处理服务
///
/// 哆啦宝异步通知(JSON 报文 + Header timestamp/token) → 主应用接收 →
/// 按 channelMchNo 组装凭证 → 连同 Header 转发到子应用用 secretKey 验签解析 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态,
/// 应答返回哆啦宝约定的文本串 success/error(子应用生成, 主应用原样返回)。
///
/// 凭 requestNum(支付时上送的商户单号 = 平台交易号)反查 PayTrade;
/// 哆啦宝仅有支付回调(退款为同步接口), 本服务仅处理支付回调。
@Slf4j
@Service
@RequiredArgsConstructor
public class DuolabaoPayCallbackService {

    private final DuolabaoChannelClient duolabaoChannelClient;

    private final DuolabaoDirectConfigAssembler configAssembler;

    private final PayCallbackService payCallbackService;

    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String mchNo, String channelMchNo, String body, String timestamp, String token) {
        // 1. 组装凭证(验签需 secretKey)
        DuolabaoSdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 2. 转发到子应用验签解析(原始报文 + Header timestamp/token)
        DuolabaoCallbackParseReq req = new DuolabaoCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        req.setNotifyTimestamp(timestamp);
        req.setNotifyToken(token);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        DaxResult<DuolabaoCallbackParseResp> result = duolabaoChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || Objects.isNull(result.getData()) || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("哆啦宝支付回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("哆啦宝支付回调验签失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return DuolabaoCode.NOTIFY_FAIL;
        }

        // 3. 构建框架回调数据
        DuolabaoCallbackParseResp resp = result.getData();
        CallbackData callbackData = this.buildCallbackData(resp);
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("哆啦宝支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return DuolabaoCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        // 业务处理成功, 返回哆啦宝约定文本应答
        return resp.getAckBody();
    }

    /// 构建框架回调数据
    private CallbackData buildCallbackData(DuolabaoCallbackParseResp resp) {
        CallbackData data = new CallbackData();
        // resp.outTradeNo 是支付时上送的 requestNum = 平台 tradeNo
        data.setTradeNo(resp.getOutTradeNo());
        // resp.outOrderNo 是哆啦宝订单号(orderNum)
        data.setOutTradeNo(resp.getOutOrderNo());
        // 完成时间(子应用已解析为 OffsetDateTime)
        data.setFinishTime(resp.getFinishTime());
        // 买家标识
        data.setBuyerId(resp.getBuyerId());
        // 交易状态(子应用已标准化: SUCCESS/FAIL)
        data.setTradeStatus(resp.getTradeStatus());
        return data;
    }
}
