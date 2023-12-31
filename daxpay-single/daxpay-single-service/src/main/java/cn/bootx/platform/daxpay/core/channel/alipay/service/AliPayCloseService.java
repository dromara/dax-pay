package cn.bootx.platform.daxpay.core.channel.alipay.service;

import cn.bootx.platform.common.spring.exception.RetryableException;
import cn.bootx.platform.daxpay.code.AliPayCode;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.response.AlipayTradeCloseResponse;
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
     * 关闭支付 此处使用交易关闭接口, 支付宝支持 交易关闭 和 交易撤销 两种关闭订单的方式, 区别如下
     * 交易关闭: 只有订单在未支付的状态下才可以进行关闭, 商户不需要额外申请就有此接口的权限
     * 交易撤销: 如果用户支付成功，会将此订单资金退还给用户. 限制时间为1天，过了24小时，该接口无法再使用。可以视为一个特殊的接口, 需要专门签约这个接口的权限
     *
     * TODO 如果返回已经关闭, 也视为关闭成功
     */
    @Retryable(value = RetryableException.class)
    public void close(PayOrder payOrder) {
        AlipayTradeCloseModel model = new AlipayTradeCloseModel();
        model.setOutTradeNo(String.valueOf(payOrder.getId()));

        try {
            AlipayTradeCloseResponse response = AliPayApi.tradeCloseToResponse(model);
            if (!Objects.equals(AliPayCode.SUCCESS, response.getCode())) {
                log.error("网关返回关闭失败: {}", response.getSubMsg());
                throw new PayFailureException(response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            log.error("关闭订单失败:", e);
            throw new PayFailureException("关闭订单失败");
        }
    }

}
