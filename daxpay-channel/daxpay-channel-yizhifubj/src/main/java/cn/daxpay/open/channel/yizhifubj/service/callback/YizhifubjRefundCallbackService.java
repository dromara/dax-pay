package cn.daxpay.open.channel.yizhifubj.service.callback;

import cn.daxpay.open.channel.yizhifubj.client.YizhifubjChannelClient;
import cn.daxpay.open.channel.yizhifubj.client.credential.YizhifubjSdkCredential;
import cn.daxpay.open.channel.yizhifubj.client.req.YizhifubjCallbackParseReq;
import cn.daxpay.open.channel.yizhifubj.client.resp.YizhifubjCallbackParseResp;
import cn.daxpay.open.channel.yizhifubj.code.YizhifubjCode;
import cn.daxpay.open.channel.yizhifubj.service.direct.YizhifubjDirectConfigAssembler;
import cn.daxpay.open.payment.trade.runtime.bo.RefundCallbackData;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.service.callback.RefundCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/// # 一直付退款回调处理服务
///
/// 一直付退款异步通知(退款同步受理的兜底通道) → 主应用接收 → 组装凭证 → 转发子应用解密验签 → 记录退款结果。
/// 应答返回明文串 SUCCESS(按一直付规约, 无需加密应答)。
///
/// 退款单状态流转已接入 [RefundCallbackService], 由框架完成终态守卫与状态更新
/// (退款同步受理已置成功时回调幂等跳过)。
@Slf4j
@Service
@RequiredArgsConstructor
public class YizhifubjRefundCallbackService {

    private final YizhifubjChannelClient yizhifubjChannelClient;
    private final YizhifubjDirectConfigAssembler configAssembler;
    private final RefundCallbackService refundCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 退款回调处理
    public String refundHandle(String mchNo, String channelMchNo, String body, String encryptKey) {
        // 1. 组装凭证
        YizhifubjSdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 2. 转发到子应用解密验签
        YizhifubjCallbackParseReq req = new YizhifubjCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        req.setEncryptKey(encryptKey);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        DaxResult<YizhifubjCallbackParseResp> result = yizhifubjChannelClient.parseRefundCallback(req);
        if (result.getCode() != 0 || Objects.isNull(result.getData()) || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("一直付退款回调验签失败: channelMchNo={}", channelMchNo);
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("一直付退款回调验签失败");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return YizhifubjCode.NOTIFY_ACK_FAIL;
        }

        // 3. 构建退款回调数据, 交框架更新退款单状态
        YizhifubjCallbackParseResp resp = result.getData();
        RefundCallbackData callbackData = new RefundCallbackData();
        // resp.outTradeNo 为退款时传入的 requestId = 平台退款关联号
        callbackData.setRefundNo(resp.getOutTradeNo());
        callbackData.setFinishTime(resp.getFinishTime());
        if (Objects.equals(YizhifubjCode.TRADE_STATUS_SUCCESS, resp.getTradeStatus())) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("一直付退款状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setCallbackData(notify);
        try {
            refundCallbackService.refundCallback(callbackData);
        } catch (Exception e) {
            log.error("一直付退款回调业务处理失败: refundNo={}", callbackData.getRefundNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.saveRefund(channelMchNo, callbackData);
            return YizhifubjCode.NOTIFY_ACK_FAIL;
        }
        payCallbackRecordService.saveRefund(channelMchNo, callbackData);
        // 业务处理成功, 返回明文应答串
        return YizhifubjCode.NOTIFY_ACK_SUCCESS;
    }
}
