package cn.bootx.platform.daxpay.service.core.channel.alipay.service;

import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.code.AliPayCode;
import cn.bootx.platform.daxpay.service.common.context.RefundLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.util.PayUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.ijpay.alipay.AliPayApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 支付宝退款服务
 * @author xxm
 * @since 2023/12/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayRefundService {

    /**
     * 退款, 调用支付宝退款
     */
    public void refund(PayOrder payOrder, int amount) {
        AlipayTradeRefundModel refundModel = new AlipayTradeRefundModel();
        refundModel.setOutTradeNo(String.valueOf(payOrder.getId()));

        String refundAmount = String.valueOf(amount*0.01);
        refundModel.setRefundAmount(refundAmount);

        // 设置退款信息
        RefundLocal refundInfo = PaymentContextLocal.get().getRefundInfo();
        refundInfo.setGatewayRequestNo(PayUtil.getRefundNo());
        refundModel.setOutRequestNo(refundInfo.getGatewayRequestNo());
        try {
            AlipayTradeRefundResponse response = AliPayApi.tradeRefundToResponse(refundModel);
            if (!Objects.equals(AliPayCode.SUCCESS, response.getCode())) {
                refundInfo.setErrorMsg(response.getSubMsg());
                refundInfo.setErrorCode(response.getCode());
                log.error("网关返回退款失败: {}", response.getSubMsg());
                throw new PayFailureException(response.getSubMsg());
            }
        }
        catch (AlipayApiException e) {
            log.error("订单退款失败:", e);
            refundInfo.setErrorMsg(e.getErrMsg());
            refundInfo.setErrorCode(e.getErrCode());
            throw new PayFailureException("订单退款失败");
        }
    }
}
