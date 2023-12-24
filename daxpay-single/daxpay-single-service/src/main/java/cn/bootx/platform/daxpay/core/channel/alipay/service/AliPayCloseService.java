package cn.bootx.platform.daxpay.core.channel.alipay.service;

import cn.bootx.platform.common.spring.exception.RetryableException;
import cn.bootx.platform.daxpay.code.AliPayCode;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeCancelModel;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.ijpay.alipay.AliPayApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 支付宝支付取消和支付关闭
 *
 * @author xxm
 * @since 2021/4/20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayCloseService {

    /**
     * 关闭支付
     */
    @Retryable(value = RetryableException.class)
    public void cancelRemote(PayOrder payOrder) {
        // 只有部分需要调用支付宝网关进行关闭
        AlipayTradeCancelModel model = new AlipayTradeCancelModel();
        model.setOutTradeNo(String.valueOf(payOrder.getId()));

        try {
            AlipayTradeCancelResponse response = AliPayApi.tradeCancelToResponse(model);
            if (!Objects.equals(AliPayCode.SUCCESS, response.getCode())) {
                log.error("网关返回撤销失败: {}", response.getSubMsg());
                throw new PayFailureException(response.getSubMsg());
            }
        }
        catch (AlipayApiException e) {
            log.error("关闭订单失败:", e);
            throw new PayFailureException("关闭订单失败");
        }
    }

}
