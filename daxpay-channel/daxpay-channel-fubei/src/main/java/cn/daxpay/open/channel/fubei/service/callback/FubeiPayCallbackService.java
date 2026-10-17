package cn.daxpay.open.channel.fubei.service.callback;

import cn.daxpay.open.channel.fubei.client.FubeiChannelClient;
import cn.daxpay.open.channel.fubei.client.credential.FubeiSdkCredential;
import cn.daxpay.open.channel.fubei.client.req.FubeiCallbackParseReq;
import cn.daxpay.open.channel.fubei.client.resp.FubeiCallbackParseResp;
import cn.daxpay.open.channel.fubei.code.FubeiCode;
import cn.daxpay.open.channel.fubei.service.direct.FubeiDirectConfigAssembler;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.service.callback.PayCallbackService;
import cn.daxpay.open.platform.common.json.util.JsonUtil;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/// # 付呗支付回调处理服务
///
/// 付呗异步通知(form-urlencoded 全参数 + MD5 签名) → 主应用接收 → 按 channelMchNo 组装凭证 →
/// 参数集合 JSON 序列化转发到子应用用 app_secret 验签 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 应答为纯文本约定(对齐参考插件): 成功处理 `success` / 验签或处理失败 `fail` /
/// 业务状态非成功 `status=原始状态码`(让付呗感知状态未推进, 按其重试策略处理)。
@Slf4j
@Service
@RequiredArgsConstructor
public class FubeiPayCallbackService {

    private final FubeiChannelClient fubeiChannelClient;
    private final FubeiDirectConfigAssembler configAssembler;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String mchNo, String channelMchNo, Map<String, String> params) {
        // 1. 组装凭证(app_secret 验签)
        FubeiSdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 2. 参数集合 JSON 序列化后转发到子应用验签解析
        String body = JsonUtil.toJsonStr(params);
        FubeiCallbackParseReq req = new FubeiCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        DaxResult<FubeiCallbackParseResp> result = fubeiChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || Objects.isNull(result.getData()) || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("付呗支付回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(params);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("付呗支付回调验签失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return FubeiCode.NOTIFY_FAIL;
        }

        // 3. 构建回调数据交由框架处理
        FubeiCallbackParseResp resp = result.getData();
        CallbackData callbackData = this.buildCallbackData(resp);
        callbackData.setCallbackData(params);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("付呗支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return FubeiCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);

        // 4. 应答: 业务状态非成功时回传原始状态码, 成功回传 success
        if (!Objects.equals(FubeiCode.TRADE_STATUS_SUCCESS, resp.getTradeStatus())) {
            return "status=" + resp.getTradeStatus();
        }
        return FubeiCode.NOTIFY_SUCCESS;
    }

    /// 构建框架回调数据
    private CallbackData buildCallbackData(FubeiCallbackParseResp resp) {
        CallbackData data = new CallbackData();
        // resp.outTradeNo 是支付时传入的 merchant_order_sn = 平台 tradeNo
        data.setTradeNo(resp.getOutTradeNo());
        // resp.outOrderNo 是付呗订单号 order_sn
        data.setOutTradeNo(resp.getOutOrderNo());
        // 完成时间(子应用已解析为 OffsetDateTime)
        data.setFinishTime(resp.getFinishTime());
        // 交易状态(SUCCESS 成功, 其余传原始值)
        data.setTradeStatus(this.mapStatus(resp.getTradeStatus()));
        return data;
    }

    /// 付呗订单状态码 → CallbackStatusEnum
    private String mapStatus(String tradeStatus) {
        if (FubeiCode.TRADE_STATUS_SUCCESS.equals(tradeStatus)) {
            return CallbackStatusEnum.SUCCESS.getCode();
        }
        // 非成功状态直接传原始值
        return tradeStatus;
    }
}
