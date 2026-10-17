package cn.daxpay.open.channel.yizhifubj.service.callback;

import cn.daxpay.open.channel.yizhifubj.client.YizhifubjChannelClient;
import cn.daxpay.open.channel.yizhifubj.client.credential.YizhifubjSdkCredential;
import cn.daxpay.open.channel.yizhifubj.client.req.YizhifubjCallbackParseReq;
import cn.daxpay.open.channel.yizhifubj.client.resp.YizhifubjCallbackParseResp;
import cn.daxpay.open.channel.yizhifubj.code.YizhifubjCode;
import cn.daxpay.open.channel.yizhifubj.service.direct.YizhifubjDirectConfigAssembler;
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

/// # 一直付支付回调处理服务
///
/// 一直付异步通知(body 密文 + encryptKey 头) → 主应用接收 → 按 channelMchNo 组装凭证 →
/// 转发到子应用用商户 pfx 解密 + 平台证书验签 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态,
/// 应答返回明文串 SUCCESS(按一直付规约, 无需加密应答)。
@Slf4j
@Service
@RequiredArgsConstructor
public class YizhifubjPayCallbackService {

    private final YizhifubjChannelClient yizhifubjChannelClient;
    private final YizhifubjDirectConfigAssembler configAssembler;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String mchNo, String channelMchNo, String body, String encryptKey) {
        // 1. 组装凭证(解密需商户 pfx + 平台证书)
        YizhifubjSdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 2. 转发到子应用解密验签
        YizhifubjCallbackParseReq req = new YizhifubjCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        req.setEncryptKey(encryptKey);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        DaxResult<YizhifubjCallbackParseResp> result = yizhifubjChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || Objects.isNull(result.getData()) || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("一直付支付回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("一直付支付回调验签失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return YizhifubjCode.NOTIFY_ACK_FAIL;
        }

        // 3. 构建回调数据交由框架处理
        YizhifubjCallbackParseResp resp = result.getData();
        CallbackData callbackData = this.buildCallbackData(resp);
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("一直付支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return YizhifubjCode.NOTIFY_ACK_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        // 业务处理成功, 返回明文应答串
        return YizhifubjCode.NOTIFY_ACK_SUCCESS;
    }

    /// 构建框架回调数据
    private CallbackData buildCallbackData(YizhifubjCallbackParseResp resp) {
        CallbackData data = new CallbackData();
        // resp.outTradeNo 是支付时传入的 requestId = 平台 tradeNo
        data.setTradeNo(resp.getOutTradeNo());
        // resp.outOrderNo 是一直付通道流水号 serialNumber
        data.setOutTradeNo(resp.getOutOrderNo());
        // 完成时间(子应用已解析为 OffsetDateTime)
        data.setFinishTime(resp.getFinishTime());
        // 交易状态映射(SUCCESS 成功 / 其他原样透传)
        data.setTradeStatus(this.mapStatus(resp.getTradeStatus()));
        return data;
    }

    /// 一直付交易状态码 → CallbackStatusEnum
    private String mapStatus(String tradeStatus) {
        if (YizhifubjCode.TRADE_STATUS_SUCCESS.equals(tradeStatus)) {
            return CallbackStatusEnum.SUCCESS.getCode();
        }
        if (YizhifubjCode.TRADE_STATUS_FAIL.equals(tradeStatus) || YizhifubjCode.TRADE_STATUS_CANCEL.equals(tradeStatus)) {
            return CallbackStatusEnum.FAIL.getCode();
        }
        // 非成功状态直接传原始值
        return tradeStatus;
    }
}
