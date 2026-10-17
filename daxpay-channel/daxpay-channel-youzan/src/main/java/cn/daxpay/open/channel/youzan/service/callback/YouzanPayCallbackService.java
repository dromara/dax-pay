package cn.daxpay.open.channel.youzan.service.callback;

import cn.daxpay.open.channel.youzan.client.YouzanChannelClient;
import cn.daxpay.open.channel.youzan.client.credential.YouzanSdkCredential;
import cn.daxpay.open.channel.youzan.client.req.YouzanCallbackParseReq;
import cn.daxpay.open.channel.youzan.client.resp.YouzanCallbackParseResp;
import cn.daxpay.open.channel.youzan.code.YouzanCode;
import cn.daxpay.open.channel.youzan.service.direct.YouzanDirectConfigAssembler;
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

/// # 有赞支付回调处理服务
///
/// 有赞异步通知(JSON body + 请求头验签材料) → 主应用接收 → 按 channelMchNo 组装凭证 →
/// 转发到子应用用有赞平台公钥验签 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态,
/// 应答返回 `{"success":true}` JSON 规约串(对齐参考实现, 无需子应用组装)。
@Slf4j
@Service
@RequiredArgsConstructor
public class YouzanPayCallbackService {

    private final YouzanChannelClient youzanChannelClient;
    private final YouzanDirectConfigAssembler configAssembler;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String mchNo, String channelMchNo, String body, String sign, String timestamp, String noncestr) {
        // 1. 组装凭证(验签需有赞平台公钥)
        YouzanSdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 2. 转发到子应用验签解析
        YouzanCallbackParseReq req = new YouzanCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        req.setSign(sign);
        req.setTimestamp(timestamp);
        req.setNoncestr(noncestr);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        DaxResult<YouzanCallbackParseResp> result = youzanChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || Objects.isNull(result.getData()) || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("有赞支付回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("有赞支付回调验签失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return YouzanCode.NOTIFY_FAIL;
        }

        // 3. 构建回调数据交由框架处理
        YouzanCallbackParseResp resp = result.getData();
        CallbackData callbackData = this.buildCallbackData(resp);
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("有赞支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return YouzanCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        // 4. 业务处理成功, 返回有赞约定的 JSON 应答
        return YouzanCode.NOTIFY_SUCCESS;
    }

    /// 构建框架回调数据
    private CallbackData buildCallbackData(YouzanCallbackParseResp resp) {
        CallbackData data = new CallbackData();
        // resp.outTradeNo 是支付时上送的 outer_trade_no = 平台 tradeNo
        data.setTradeNo(resp.getOutTradeNo());
        // resp.outOrderNo 是有赞订单号 trade_no
        data.setOutTradeNo(resp.getOutOrderNo());
        // 完成时间(子应用已解析为 OffsetDateTime)
        data.setFinishTime(resp.getFinishTime());
        // 交易状态映射(SUCCESS 成功)
        data.setTradeStatus(this.mapStatus(resp.getTradeStatus()));
        return data;
    }

    /// 有赞交易状态码 → CallbackStatusEnum
    private String mapStatus(String tradeStatus) {
        if (YouzanCode.TRADE_STATUS_SUCCESS.equals(tradeStatus)) {
            return CallbackStatusEnum.SUCCESS.getCode();
        }
        // 非成功状态直接传原始值
        return tradeStatus;
    }
}
