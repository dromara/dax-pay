package cn.daxpay.single.service.core.channel.alipay.service;

import cn.daxpay.single.code.RefundStatusEnum;
import cn.daxpay.single.exception.pay.PayFailureException;
import cn.daxpay.single.service.code.AliPayCode;
import cn.daxpay.single.service.common.context.ErrorInfoLocal;
import cn.daxpay.single.service.common.context.RefundLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.daxpay.single.util.PayUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
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

    private final AliPayConfigService aliPayConfigService;

    /**
     * 退款, 调用支付宝退款
     */
    public void refund(RefundOrder refundOrder, AliPayConfig config) {
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(config);

        RefundLocal refundInfo = PaymentContextLocal.get().getRefundInfo();
        ErrorInfoLocal errorInfo = PaymentContextLocal.get().getErrorInfo();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(refundOrder.getOrderNo());
        model.setOutRequestNo(refundOrder.getRefundNo());
        // 金额转换
        String refundAmount = PayUtil.conversionAmount(refundOrder.getAmount()).toString();
        model.setRefundAmount(refundAmount);
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizModel(model);

        try {
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (!Objects.equals(AliPayCode.SUCCESS, response.getCode())) {
                errorInfo.setErrorMsg(response.getSubMsg());
                errorInfo.setErrorCode(response.getCode());
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
            errorInfo.setErrorMsg(e.getErrMsg());
            errorInfo.setErrorCode(e.getErrCode());
            throw new PayFailureException("订单退款失败");
        }
    }
}
