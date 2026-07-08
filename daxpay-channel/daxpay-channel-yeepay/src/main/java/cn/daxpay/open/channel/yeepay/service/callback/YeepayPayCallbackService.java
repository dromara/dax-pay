package cn.daxpay.open.channel.yeepay.service.callback;

import cn.daxpay.open.channel.yeepay.client.YeepayChannelClient;
import cn.daxpay.open.channel.yeepay.client.credential.YeepaySdkCredential;
import cn.daxpay.open.channel.yeepay.client.req.YeepayCallbackParseReq;
import cn.daxpay.open.channel.yeepay.client.resp.YeepayCallbackParseResp;
import cn.daxpay.open.channel.yeepay.code.YeepayCode;
import cn.daxpay.open.channel.yeepay.service.direct.YeepayDirectConfigAssembler;
import cn.daxpay.open.payment.common.callback.CallbackData;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.service.PayCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易宝支付回调处理服务
///
/// 易宝异步通知(form 表单) → 主应用接收 → 按 channelMchNo 组装凭证 →
/// 转发到子应用用 RSA2048 数字信封解密验签 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 易宝通知含 response(密文) 与 customerIdentification(appKey) 两个 form 参数。
@Slf4j
@Service
@RequiredArgsConstructor
public class YeepayPayCallbackService {

    private final YeepayChannelClient yeepayChannelClient;
    private final YeepayDirectConfigAssembler configAssembler;
    private final PayCallbackService payCallbackService;

    /// 支付回调处理
    public String payHandle(String mchNo, String channelMchNo, HttpServletRequest request) {
        // 1. 提取易宝通知参数(form 表单: response 密文 + customerIdentification appKey)
        String response = request.getParameter("response");
        String customerIdentification = request.getParameter("customerIdentification");

        // 2. 组装凭证
        YeepaySdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 3. 转发到子应用解密验签
        YeepayCallbackParseReq req = new YeepayCallbackParseReq();
        req.setCredential(credential);
        req.setResponse(response);
        req.setCustomerIdentification(customerIdentification);
        DaxResult<YeepayCallbackParseResp> result = yeepayChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || result.getData() == null || !result.getData().isVerified()) {
            log.error("易宝支付回调验签失败: channelMchNo={}", channelMchNo);
            return YeepayCode.NOTIFY_FAIL;
        }

        // 4. 构建回调数据交由框架处理
        YeepayCallbackParseResp resp = result.getData();
        CallbackData callbackData = this.buildCallbackData(resp);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("易宝支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            return YeepayCode.NOTIFY_FAIL;
        }
        return YeepayCode.NOTIFY_SUCCESS;
    }

    /// 构建框架回调数据
    private CallbackData buildCallbackData(YeepayCallbackParseResp resp) {
        CallbackData data = new CallbackData();
        // resp.outTradeNo 是支付时传入的 orderId = 平台 tradeNo
        data.setTradeNo(resp.getOutTradeNo());
        // resp.targetOrderId 是易宝 uniqueOrderNo
        data.setOutTradeNo(resp.getTargetOrderId());
        // 完成时间(子应用已解析为 OffsetDateTime)
        data.setFinishTime(resp.getFinishTime());
        // 交易状态映射
        data.setTradeStatus(this.mapStatus(resp.getTradeStatus()));
        return data;
    }

    /// 统一状态码 → CallbackStatusEnum
    private String mapStatus(String tradeStatus) {
        if (YeepayCode.TRADE_STATUS_SUCCESS.equals(tradeStatus)) {
            return CallbackStatusEnum.SUCCESS.getCode();
        }
        if (YeepayCode.TRADE_STATUS_FAIL.equals(tradeStatus) || YeepayCode.TRADE_STATUS_CLOSED.equals(tradeStatus)) {
            return CallbackStatusEnum.FAIL.getCode();
        }
        // 非成功状态直接传原始值
        return tradeStatus;
    }
}
