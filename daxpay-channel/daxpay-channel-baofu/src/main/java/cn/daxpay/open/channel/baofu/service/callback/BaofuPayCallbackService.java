package cn.daxpay.open.channel.baofu.service.callback;

import cn.daxpay.open.channel.baofu.client.BaofuChannelClient;
import cn.daxpay.open.channel.baofu.client.credential.BaofuSdkCredential;
import cn.daxpay.open.channel.baofu.client.req.BaofuCallbackParseReq;
import cn.daxpay.open.channel.baofu.client.resp.BaofuCallbackParseResp;
import cn.daxpay.open.channel.baofu.code.BaofuCode;
import cn.daxpay.open.channel.baofu.service.direct.BaofuDirectConfigAssembler;
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

/// # 宝付支付回调处理服务
///
/// 宝付异步通知(form 提交 dataContent + signStr) → 主应用接收 → 按 channelMchNo 组装凭证 →
/// 转发到子应用用宝付平台证书验签 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态,
/// 应答返回纯文本 OK / FAIL(对齐参考实现, 无需子应用组装)。
///
/// 宝付退款为同步终态无退款异步通知, 仅本支付回调入口。
@Slf4j
@Service
@RequiredArgsConstructor
public class BaofuPayCallbackService {

    private final BaofuChannelClient baofuChannelClient;
    private final BaofuDirectConfigAssembler configAssembler;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String mchNo, String channelMchNo, String dataContent, String signStr) {
        // 1. 组装凭证(验签需宝付平台证书)
        BaofuSdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 2. 转发到子应用验签解析
        BaofuCallbackParseReq req = new BaofuCallbackParseReq();
        req.setCredential(credential);
        req.setDataContent(dataContent);
        req.setSignStr(signStr);
        Map<String, Object> notify = new HashMap<>();
        notify.put("dataContent", dataContent);
        DaxResult<BaofuCallbackParseResp> result = baofuChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || Objects.isNull(result.getData()) || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("宝付支付回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("宝付支付回调验签失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return BaofuCode.NOTIFY_FAIL;
        }

        // 3. 构建回调数据交由框架处理
        BaofuCallbackParseResp resp = result.getData();
        CallbackData callbackData = this.buildCallbackData(resp);
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("宝付支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return BaofuCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        // 4. 业务处理成功, 返回宝付约定的纯文本应答
        return BaofuCode.NOTIFY_SUCCESS;
    }

    /// 构建框架回调数据
    private CallbackData buildCallbackData(BaofuCallbackParseResp resp) {
        CallbackData data = new CallbackData();
        // resp.outTradeNo 是支付时传入的 outTradeNo = 平台 tradeNo
        data.setTradeNo(resp.getOutTradeNo());
        // resp.outOrderNo 是宝付交易号 tradeNo
        data.setOutTradeNo(resp.getOutOrderNo());
        // 完成时间(子应用已解析为 OffsetDateTime)
        data.setFinishTime(resp.getFinishTime());
        // 交易状态映射(SUCCESS 成功, 其他非成功)
        data.setTradeStatus(this.mapStatus(resp.getTradeStatus()));
        return data;
    }

    /// 宝付交易状态码 → CallbackStatusEnum
    private String mapStatus(String tradeStatus) {
        if (BaofuCode.TRADE_STATUS_SUCCESS.equals(tradeStatus)) {
            return CallbackStatusEnum.SUCCESS.getCode();
        }
        // 非成功状态直接传原始值
        return tradeStatus;
    }
}
