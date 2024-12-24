package org.dromara.daxpay.channel.wechat.service.refund;

import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.dromara.daxpay.core.enums.RefundStatusEnum;
import org.dromara.daxpay.core.exception.TradeFailException;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.service.bo.trade.RefundResultBo;
import org.dromara.daxpay.service.entity.order.refund.RefundOrder;
import org.springframework.stereotype.Service;

/**
 * 微信退款服务 v2
 * @author xxm
 * @since 2023/12/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatSubRefundV2Service {

    private final WechatPayConfigService wechatPayConfigService;

    /**
     * 退款方法
     * 微信需要同时传输订单金额或退款金额
     */
    public RefundResultBo refund(RefundOrder refundOrder, WechatPayConfig config) {
        RefundResultBo refundInfo = new RefundResultBo();

        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        WxPayRefundRequest request = new WxPayRefundRequest()
                .setOutRefundNo(refundOrder.getRefundNo())
                .setNotifyUrl(wechatPayConfigService.getRefundNotifyUrl(true))
                .setRefundDesc(refundOrder.getReason())
                .setOutTradeNo(refundOrder.getOrderNo())
                .setTotalFee(PayUtil.convertCentAmount(refundOrder.getOrderAmount()))
                .setRefundFee(PayUtil.convertCentAmount(refundOrder.getAmount()));
        request.setSubMchId(config.getSubMchId()).setSubAppId(config.getSubAppId());
        try {
            WxPayRefundResult result = wxPayService.refund(request);
            // 微信V2版本的退款不会返回状态, 需要等待回调或手动查询
            refundInfo.setStatus(RefundStatusEnum.PROGRESS)
                    .setOutRefundNo(result.getRefundId());
        } catch (WxPayException e) {
            log.error("微信退款V2失败", e);
            throw new TradeFailException("微信退款V2失败: " + e.getMessage());
        }
        return refundInfo;
    }
}
