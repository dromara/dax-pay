package cn.daxpay.multi.channel.wechat.service.refund;

import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.channel.wechat.enums.WechatRefundStatusEnum;
import cn.daxpay.multi.channel.wechat.service.config.WechatPayConfigService;
import cn.daxpay.multi.core.exception.TradeFailException;
import cn.daxpay.multi.core.util.PayUtil;
import cn.daxpay.multi.service.common.context.RefundLocal;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import cn.daxpay.multi.service.entity.order.refund.RefundOrder;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
    public void refund(RefundOrder refundOrder, WechatPayConfig config) {
        RefundLocal refundInfo = PaymentContextLocal.get().getRefundInfo();

        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        WxPayRefundV3Request.Amount amount = new WxPayRefundV3Request.Amount()
                .setRefund(PayUtil.convertCentAmount(refundOrder.getAmount()))
                .setTotal(PayUtil.convertCentAmount(refundOrder.getOrderAmount()));
        WxPayRefundV3Request request = new WxPayRefundV3Request()
                .setOutRefundNo(refundOrder.getRefundNo())
                .setNotifyUrl(wechatPayConfigService.getRefundNotifyUrl())
                .setReason(refundOrder.getReason())
                .setOutTradeNo(refundOrder.getOrderNo())
                .setAmount(amount);
        try {
            WxPayRefundV3Result result = wxPayService.refundV3(request);
            String successTime = result.getSuccessTime();
            LocalDateTime parse = LocalDateTimeUtil.parse(successTime, "yyyy-MM-dd'T'HH:mm:ss");

            WechatRefundStatusEnum statusEnum = WechatRefundStatusEnum.findByCode(result.getStatus());
            refundInfo.setStatus(statusEnum.getRefundStatus())
                    .setFinishTime(parse)
                    .setOutRefundNo(result.getRefundId());
        } catch (WxPayException e) {
            log.error("微信退款V3失败", e);
            throw new TradeFailException("微信退款V3失败: "+e.getMessage());
        }
    }
}
