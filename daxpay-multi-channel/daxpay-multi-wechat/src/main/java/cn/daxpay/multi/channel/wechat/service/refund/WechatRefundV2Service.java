package cn.daxpay.multi.channel.wechat.service.refund;

import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.channel.wechat.service.config.WechatPayConfigService;
import cn.daxpay.multi.core.enums.RefundStatusEnum;
import cn.daxpay.multi.core.exception.TradeFailException;
import cn.daxpay.multi.core.util.PayUtil;
import cn.daxpay.multi.service.common.context.RefundLocal;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import cn.daxpay.multi.service.entity.order.refund.RefundOrder;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 微信退款服务 v2
 * @author xxm
 * @since 2023/12/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatRefundV2Service {

    private final WechatPayConfigService wechatPayConfigService;

    /**
     * 退款方法
     * 微信需要同时传输订单金额或退款金额
     */
    public void refund(RefundOrder refundOrder, WechatPayConfig config) {
        RefundLocal refundInfo = PaymentContextLocal.get()
                .getRefundInfo();

        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        WxPayRefundRequest request = new WxPayRefundRequest()
                .setOutRefundNo(refundOrder.getRefundNo())
                .setNotifyUrl(wechatPayConfigService.getRefundNotifyUrl())
                .setRefundDesc(refundOrder.getReason())
                .setOutTradeNo(refundOrder.getOrderNo())
                .setTotalFee(PayUtil.convertCentAmount(refundOrder.getOrderAmount()))
                .setRefundFee(PayUtil.convertCentAmount(refundOrder.getAmount()));
        try {
            WxPayRefundResult result = wxPayService.refund(request);
            // 微信V2版本的退款不会返回状态, 需要等待回调或手动查询
            refundInfo.setStatus(RefundStatusEnum.PROGRESS)
                    .setOutRefundNo(result.getRefundId());
        } catch (WxPayException e) {
            log.error("微信退款V2失败", e);
            throw new TradeFailException("微信退款V2失败: " + e.getMessage());
        }
    }
}
