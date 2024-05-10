package cn.daxpay.single.service.core.channel.alipay.service;

import cn.daxpay.single.code.RefundStatusEnum;
import cn.daxpay.single.exception.pay.PayFailureException;
import cn.daxpay.single.service.code.AliPayCode;
import cn.daxpay.single.service.common.context.RefundLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.ijpay.alipay.AliPayApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public void refund(RefundOrder refundOrder) {
        RefundLocal refundInfo = PaymentContextLocal.get().getRefundInfo();
        AlipayTradeRefundModel refundModel = new AlipayTradeRefundModel();
        refundModel.setOutTradeNo(refundOrder.getOrderNo());
        refundModel.setOutRequestNo(refundOrder.getRefundNo());
        // 金额转换
        String refundAmount = String.valueOf(refundOrder.getAmount()*0.01);
        refundModel.setRefundAmount(refundAmount);

        // 设置退款信息
        try {
            AlipayTradeRefundResponse response = AliPayApi.tradeRefundToResponse(refundModel);
            if (!Objects.equals(AliPayCode.SUCCESS, response.getCode())) {
                refundInfo.setErrorMsg(response.getSubMsg());
                refundInfo.setErrorCode(response.getCode());
                log.error("网关返回退款失败: {}", response.getSubMsg());
                throw new PayFailureException(response.getSubMsg());
            }
            // 默认为退款中状态
            refundInfo.setStatus(RefundStatusEnum.PROGRESS)
                    .setOutRefundNo(response.getTradeNo());

            // 接口返回fund_change=Y为退款成功，fund_change=N或无此字段值返回时需通过退款查询接口进一步确认退款状态
            if (response.getFundChange().equals("Y")){
                refundInfo.setStatus(RefundStatusEnum.SUCCESS);
                refundInfo.setFinishTime(LocalDateTime.now());
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
