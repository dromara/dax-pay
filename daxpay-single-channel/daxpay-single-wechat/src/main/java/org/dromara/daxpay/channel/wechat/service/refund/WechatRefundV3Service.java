package org.dromara.daxpay.channel.wechat.service.refund;

import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.enums.WechatRefundStatusEnum;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.dromara.daxpay.channel.wechat.util.WechatPayUtil;
import org.dromara.daxpay.core.exception.TradeFailException;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.service.bo.trade.RefundResultBo;
import org.dromara.daxpay.service.entity.order.refund.RefundOrder;
import org.springframework.stereotype.Service;

/**
 * 微信退款 v3
 * @author xxm
 * @since 2024/7/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatRefundV3Service {

    private final WechatPayConfigService wechatPayConfigService;

    /**
     * 退款
     */
    public RefundResultBo refund(RefundOrder refundOrder, WechatPayConfig config) {
        RefundResultBo refundInfo = new RefundResultBo();

        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        var amount = new WxPayRefundV3Request.Amount()
                .setCurrency(WxPayConstants.CurrencyType.CNY)
                .setRefund(PayUtil.convertCentAmount(refundOrder.getAmount()))
                .setTotal(PayUtil.convertCentAmount(refundOrder.getOrderAmount()));
        WxPayRefundV3Request request = new WxPayRefundV3Request()
                .setOutRefundNo(refundOrder.getRefundNo())
                .setNotifyUrl(wechatPayConfigService.getRefundNotifyUrl(true))
                .setReason(refundOrder.getReason())
                .setOutTradeNo(refundOrder.getOrderNo())
                .setAmount(amount);
        try {
            WxPayRefundV3Result result = wxPayService.refundV3(request);

            WechatRefundStatusEnum statusEnum = WechatRefundStatusEnum.findByCode(result.getStatus());
            refundInfo.setStatus(statusEnum.getRefundStatus())
                    .setFinishTime(WechatPayUtil.parseV3(result.getSuccessTime()))
                    .setOutRefundNo(result.getRefundId());
        } catch (WxPayException e) {
            log.error("微信退款V3失败", e);
            throw new TradeFailException("微信退款V3失败: "+e.getMessage());
        }
        return refundInfo;
    }
}
