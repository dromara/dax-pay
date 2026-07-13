package cn.daxpay.open.channel.fuyou.service.callback;

import cn.daxpay.open.channel.fuyou.client.resp.FuyouCallbackParseResp;
import cn.daxpay.open.channel.fuyou.dao.isv.FuyouIsvKeyConfigManager;
import cn.daxpay.open.channel.fuyou.entity.isv.FuyouIsvKeyConfig;
import cn.daxpay.open.payment.trade.runtime.bo.RefundCallbackData;
import cn.daxpay.open.payment.trade.runtime.service.callback.RefundCallbackService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 富友退款回调处理服务
///
/// 富友退款异步通知 → 主应用接收 `req` 参数 → 转发子应用验签与解析 →
/// 构建 [RefundCallbackData] 交由 [RefundCallbackService] 更新退款单状态。
///
/// 富友退款回调用 mchnt_order_no 作为退款单标识, 主应用据此作为 refundNo 反查退款单。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouRefundCallbackService {

    private final FuyouPayCallbackService fuyouPayCallbackService;
    private final FuyouIsvKeyConfigManager fuyouIsvKeyConfigManager;
    private final RefundCallbackService refundCallbackService;

    /// 退款回调处理
    public String refundHandle(String reqParam) {
        if (StrUtil.isBlank(reqParam)) {
            log.error("富友退款回调: req 参数为空");
            return FuyouPayCallbackService.RESP_FAIL;
        }
        FuyouIsvKeyConfig keyConfig = fuyouIsvKeyConfigManager.findByProduct(ProductEnum.FUYOU_PAY.getCode())
                .orElse(null);
        if (keyConfig == null || StrUtil.isBlank(keyConfig.getPublicKey())) {
            log.error("富友退款回调: 服务商密钥未配置, 无法验签");
            return FuyouPayCallbackService.RESP_FAIL;
        }

        FuyouCallbackParseResp resp = fuyouPayCallbackService.parse(reqParam, keyConfig.getPublicKey(), true);
        if (resp == null || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("富友退款回调验签失败");
            return FuyouPayCallbackService.RESP_FAIL;
        }

        RefundCallbackData callbackData = new RefundCallbackData();
        // mchnt_order_no 作为退款单标识反查退款单
        callbackData.setRefundNo(resp.getOutTradeNo());
        callbackData.setOutRefundNo(resp.getOutRefundNo());
        if (Objects.equals(resp.getTradeStatus(), "SUCCESS")) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("富友退款状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        refundCallbackService.refundCallback(callbackData);
        return FuyouPayCallbackService.RESP_SUCCESS;
    }
}
