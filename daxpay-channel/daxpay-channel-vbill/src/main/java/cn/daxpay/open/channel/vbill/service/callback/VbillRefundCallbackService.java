package cn.daxpay.open.channel.vbill.service.callback;

import cn.daxpay.open.channel.vbill.client.resp.VbillCallbackParseResp;
import cn.daxpay.open.channel.vbill.dao.isv.VbillIsvKeyConfigManager;
import cn.daxpay.open.channel.vbill.entity.isv.VbillIsvKeyConfig;
import cn.daxpay.open.payment.trade.runtime.bo.RefundCallbackData;
import cn.daxpay.open.payment.trade.runtime.service.callback.RefundCallbackService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/// # 随行付退款回调处理服务
///
/// 随行付退款异步通知 → 主应用接收 → 转发子应用验签与解析 →
/// 构建 [RefundCallbackData] 交由 [RefundCallbackService] 更新退款单状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class VbillRefundCallbackService {

    private final VbillPayCallbackService vbillPayCallbackService;
    private final VbillIsvKeyConfigManager vbillIsvKeyConfigManager;
    private final RefundCallbackService refundCallbackService;

    /// 退款回调处理
    public Map<String, String> refundHandle(HttpServletRequest request) {
        Map<String, String> resp = new HashMap<>(4);
        String body = JakartaServletUtil.getBody(request);
        if (StrUtil.isBlank(body)) {
            resp.put("code", VbillPayCallbackService.RESP_CODE_FAIL);
            resp.put("msg", "body 为空");
            return resp;
        }
        VbillIsvKeyConfig keyConfig = vbillIsvKeyConfigManager.findByProduct(ProductEnum.VBILL_PAY.getCode())
                .orElse(null);
        if (keyConfig == null || keyConfig.getPublicKey() == null) {
            log.error("随行付退款回调: 服务商密钥未配置, 无法验签");
            resp.put("code", VbillPayCallbackService.RESP_CODE_FAIL);
            resp.put("msg", "密钥未配置");
            return resp;
        }

        VbillCallbackParseResp parseResp = vbillPayCallbackService.parse(body, keyConfig.getPublicKey(), true);
        if (parseResp == null || !Boolean.TRUE.equals(parseResp.getSuccess())) {
            log.error("随行付退款回调验签失败");
            resp.put("code", VbillPayCallbackService.RESP_CODE_FAIL);
            resp.put("msg", "验签失败");
            return resp;
        }

        RefundCallbackData callbackData = new RefundCallbackData();
        // ordNo 作为退款单号反查退款单
        callbackData.setRefundNo(parseResp.getOutTradeNo());
        callbackData.setOutRefundNo(parseResp.getOutRefundNo());
        if (Objects.equals(parseResp.getTradeStatus(), "SUCCESS")) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("随行付退款状态非成功: " + parseResp.getTradeStatus());
        }
        callbackData.setFinishTime(parseResp.getFinishTime());
        refundCallbackService.refundCallback(callbackData);
        resp.put("code", VbillPayCallbackService.RESP_CODE_SUCCESS);
        resp.put("msg", "成功");
        return resp;
    }
}
