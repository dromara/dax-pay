package org.dromara.daxpay.channel.alipay.service.sync;

import cn.bootx.platform.core.util.JsonUtil;
import org.dromara.daxpay.channel.alipay.code.AlipayCode;
import org.dromara.daxpay.channel.alipay.entity.AliPayConfig;
import org.dromara.daxpay.channel.alipay.service.config.AlipayConfigService;
import org.dromara.daxpay.core.enums.RefundStatusEnum;
import org.dromara.daxpay.service.bo.sync.RefundSyncResultBo;
import org.dromara.daxpay.service.entity.order.refund.RefundOrder;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;

/**
 * 支付宝退款同步
 * @author xxm
 * @since 2024/7/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayRefundSyncService {

    private final AlipayConfigService aliPayConfigService;

    /**
     * 退款同步查询
     * 注意: 支付宝退款没有网关订单号, 网关订单号是支付单的
     */
    public RefundSyncResultBo syncRefundStatus(RefundOrder refundOrder,AliPayConfig aliPayConfig){
        RefundSyncResultBo syncResult = new RefundSyncResultBo();
        try {
            AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
            // 退款请求号
            model.setOutRequestNo(String.valueOf(refundOrder.getRefundNo()));
            // 商户订单号
            model.setOutTradeNo(String.valueOf(refundOrder.getOrderNo()));
            // 设置返回退款完成时间
            model.setQueryOptions(Collections.singletonList(AlipayCode.ResponseParams.GMT_REFUND_PAY));
            AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
            // 特约商户调用
            if (aliPayConfig.isIsv()){
                request.putOtherTextParam(AlipayConstants.APP_AUTH_TOKEN, aliPayConfig.getAppAuthToken());
            }
            request.setBizModel(model);
            AlipayTradeFastpayRefundQueryResponse response = aliPayConfigService.execute(request,aliPayConfig);
            syncResult.setSyncData(JsonUtil.toJsonStr(response));
            // 失败
            if (!response.isSuccess()) {
                syncResult.setSyncSuccess(false)
                        .setSyncErrorCode(response.getSubCode())
                        .setRefundStatus(RefundStatusEnum.FAIL)
                        .setSyncErrorMsg(response.getSubMsg());
                return syncResult;
            }
            String tradeStatus = response.getRefundStatus();
            // 成功
            if (Objects.equals(tradeStatus, AlipayCode.RefundStatus.REFUND_SUCCESS)){
                LocalDateTime localDateTime = LocalDateTimeUtil.of(response.getGmtRefundPay());
                return syncResult.setFinishTime(localDateTime).setRefundStatus(RefundStatusEnum.SUCCESS);
            } else {
                return syncResult.setRefundStatus(RefundStatusEnum.FAIL).setSyncErrorMsg("支付宝网关退款失败");
            }
        } catch (AlipayApiException e) {
            log.error("退款订单同步失败:", e);
            syncResult.setSyncErrorMsg(e.getErrMsg())
                    .setSyncSuccess(false)
                    .setRefundStatus(RefundStatusEnum.FAIL);
        }
        return syncResult;
    }
}
