package cn.daxpay.open.channel.leshua.service.callback;

import cn.daxpay.open.channel.leshua.client.LeshuaChannelClient;
import cn.daxpay.open.channel.leshua.client.credential.LeshuaSdkCredential;
import cn.daxpay.open.channel.leshua.client.req.LeshuaCallbackParseReq;
import cn.daxpay.open.channel.leshua.client.resp.LeshuaCallbackParseResp;
import cn.daxpay.open.channel.leshua.code.LeshuaCode;
import cn.daxpay.open.channel.leshua.dao.isv.LeshuaIsvKeyConfigManager;
import cn.daxpay.open.channel.leshua.entity.isv.LeshuaIsvKeyConfig;
import cn.daxpay.open.payment.trade.runtime.bo.RefundCallbackData;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.service.callback.RefundCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 乐刷退款回调处理服务
///
/// 乐刷退款异步通知(XML) → 主应用接收原始 body → 转发子应用用 tradeKey 做 MD5/SM3 验签 → 记录退款回调结果。
///
/// TODO 退款单状态更新待接入退款回调框架(参考斗拱 DougongRefundCallbackService)。
@Slf4j
@Service
@RequiredArgsConstructor
public class LeshuaRefundCallbackService {

    private static final String NOTIFY_SUCCESS = "000000";
    private static final String NOTIFY_FAIL = "500";

    private final LeshuaChannelClient leshuaChannelClient;
    private final LeshuaIsvKeyConfigManager leshuaIsvKeyConfigManager;
    private final RefundCallbackService refundCallbackService;

    /// 退款回调处理
    public String refundHandle(HttpServletRequest request) {
        // 1. 提取回调原始报文(XML)
        String body = JakartaServletUtil.getBody(request);

        // 2. 获取服务商密钥(用于子应用验签)
        LeshuaIsvKeyConfig keyConfig = leshuaIsvKeyConfigManager
                .findByProduct(ProductEnum.LESHUA_PAY.getCode())
                .orElse(null);
        if (keyConfig == null || StrUtil.isBlank(keyConfig.getTradeKey())) {
            log.error("乐刷退款回调: 服务商密钥未配置, 无法验签");
            return NOTIFY_FAIL;
        }

        // 3. 转发子应用验签解析
        LeshuaSdkCredential credential = new LeshuaSdkCredential();
        credential.setTradeKey(keyConfig.getTradeKey());
        credential.setSignType(keyConfig.getSignType());
        LeshuaCallbackParseReq req = new LeshuaCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        req.setCallbackType("REFUND");

        DaxResult<LeshuaCallbackParseResp> result = leshuaChannelClient.parseRefundCallback(req);
        if (result.getCode() != 0 || result.getData() == null
                || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("乐刷退款回调验签/解析失败");
            return NOTIFY_FAIL;
        }
        LeshuaCallbackParseResp resp = result.getData();

        // 4. 构建退款回调数据, 交框架更新退款单状态
        RefundCallbackData callbackData = new RefundCallbackData();
        callbackData.setRefundNo(resp.getOutTradeNo());
        callbackData.setOutRefundNo(resp.getLeshuaOrderId());
        if (Objects.equals(resp.getTradeStatus(), LeshuaCode.REFUND_STATUS_SUCCESS)) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("乐刷退款状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        refundCallbackService.refundCallback(callbackData);
        return NOTIFY_SUCCESS;
    }
}
