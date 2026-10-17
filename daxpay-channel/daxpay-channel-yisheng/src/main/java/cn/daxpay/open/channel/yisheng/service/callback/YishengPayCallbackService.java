package cn.daxpay.open.channel.yisheng.service.callback;

import cn.daxpay.open.channel.yisheng.client.YishengChannelClient;
import cn.daxpay.open.channel.yisheng.client.credential.YishengSdkCredential;
import cn.daxpay.open.channel.yisheng.client.req.YishengCallbackParseReq;
import cn.daxpay.open.channel.yisheng.client.resp.YishengCallbackParseResp;
import cn.daxpay.open.channel.yisheng.code.YishengCode;
import cn.daxpay.open.channel.yisheng.service.direct.YishengDirectConfigAssembler;
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

/// # 易生支付回调处理服务
///
/// 易生异步通知(form 提交 biz_content + sign) → 主应用接收 → 按 channelMchNo 组装凭证 →
/// 转发到子应用用易生平台公钥验签 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态,
/// 应答返回纯文本 SUCCESS / FAIL(对齐参考实现, 无需子应用组装)。
///
/// 易生退款为同步终态无退款异步通知, 仅本支付回调入口。
@Slf4j
@Service
@RequiredArgsConstructor
public class YishengPayCallbackService {

    private final YishengChannelClient yishengChannelClient;
    private final YishengDirectConfigAssembler configAssembler;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String mchNo, String channelMchNo, String bizContent, String sign) {
        // 1. 组装凭证(验签需易生平台公钥)
        YishengSdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 2. 转发到子应用验签解析
        YishengCallbackParseReq req = new YishengCallbackParseReq();
        req.setCredential(credential);
        req.setBody(bizContent);
        req.setSign(sign);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", bizContent);
        DaxResult<YishengCallbackParseResp> result = yishengChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || Objects.isNull(result.getData()) || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("易生支付回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("易生支付回调验签失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return YishengCode.NOTIFY_FAIL;
        }

        // 3. 构建回调数据交由框架处理
        YishengCallbackParseResp resp = result.getData();
        CallbackData callbackData = this.buildCallbackData(resp);
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("易生支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return YishengCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        // 4. 业务处理成功, 返回易生约定的纯文本应答
        return YishengCode.NOTIFY_SUCCESS;
    }

    /// 构建框架回调数据
    private CallbackData buildCallbackData(YishengCallbackParseResp resp) {
        CallbackData data = new CallbackData();
        // resp.outTradeNo 是支付时传入的 out_trade_no = 平台 tradeNo
        data.setTradeNo(resp.getOutTradeNo());
        // resp.outOrderNo 是易生交易号 trade_no
        data.setOutTradeNo(resp.getOutOrderNo());
        // 完成时间(子应用已解析为 OffsetDateTime)
        data.setFinishTime(resp.getFinishTime());
        // 交易状态映射(TRADE_FINISHED 成功, 其他非成功)
        data.setTradeStatus(this.mapStatus(resp.getTradeStatus()));
        return data;
    }

    /// 易生交易状态码 → CallbackStatusEnum
    private String mapStatus(String tradeStatus) {
        if (YishengCode.NOTIFY_TRADE_SUCCESS.equals(tradeStatus)) {
            return CallbackStatusEnum.SUCCESS.getCode();
        }
        // 非成功状态直接传原始值
        return tradeStatus;
    }
}
