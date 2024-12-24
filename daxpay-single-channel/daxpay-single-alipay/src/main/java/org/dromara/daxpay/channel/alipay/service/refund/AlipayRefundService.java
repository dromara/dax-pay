package org.dromara.daxpay.channel.alipay.service.refund;

import org.dromara.daxpay.channel.alipay.entity.AliPayConfig;
import org.dromara.daxpay.channel.alipay.service.config.AlipayConfigService;
import org.dromara.daxpay.core.enums.RefundStatusEnum;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.core.exception.TradeFailException;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.service.bo.trade.RefundResultBo;
import org.dromara.daxpay.service.entity.order.refund.RefundOrder;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付宝退款服务
 * @author xxm
 * @since 2023/12/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayRefundService {

    private final AlipayConfigService aliPayConfigService;

    /**
     * 退款, 调用支付宝退款
     */
    public RefundResultBo refund(RefundOrder refundOrder,AliPayConfig aliPayConfig) {
        RefundResultBo refundInfo = new RefundResultBo();
        var model = new AlipayTradeRefundModel();
        model.setOutTradeNo(refundOrder.getOrderNo());
        model.setOutRequestNo(refundOrder.getRefundNo());
        // 金额格式化
        String refundAmount = PayUtil.toDecimal(refundOrder.getAmount()).toString();
        model.setRefundAmount(refundAmount);
        // 银行卡冲退信息, 只有传输了此值, 才会触发退款回调
        model.setQueryOptions(List.of("deposit_back_info"));
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        // 特约商户调用
        if (aliPayConfig.isIsv()){
            request.putOtherTextParam(AlipayConstants.APP_AUTH_TOKEN, aliPayConfig.getAppAuthToken());
        }
        request.setBizModel(model);
        try {
            AlipayTradeRefundResponse response = aliPayConfigService.execute(request,aliPayConfig);
            if (!response.isSuccess()) {
                TradeFailException operationFailException = new TradeFailException("支付宝退款失败: "+response.getSubMsg());
                log.error("支付宝退款失败: {}", response.getSubMsg());
                throw operationFailException;
            }
            // 接口返回fund_change=Y为退款成功，fund_change=N或无此字段值返回时需通过退款查询接口进一步确认退款状态
            if (response.getFundChange().equals("Y")){
                refundInfo.setStatus(RefundStatusEnum.SUCCESS);
                refundInfo.setFinishTime(LocalDateTime.now());
            }
        }
        catch (AlipayApiException e) {
            log.error("支付宝退款失败:", e);
            throw new OperationFailException("支付宝退款失败: "+e.getErrMsg());
        }
        return refundInfo;
    }
}
