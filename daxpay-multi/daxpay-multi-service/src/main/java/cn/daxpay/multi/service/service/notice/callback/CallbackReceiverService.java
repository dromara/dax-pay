package cn.daxpay.multi.service.service.notice.callback;

import cn.daxpay.multi.service.strategy.AbsCallbackStrategy;
import cn.daxpay.multi.service.util.PaymentStrategyFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付/退款回调服务
 * @author xxm
 * @since 2024/7/20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CallbackReceiverService {


    /**
     * 支付信息回调处理
     */
    public String payHandle(HttpServletRequest request, String channel){
        var callbackStrategy = PaymentStrategyFactory.create(channel, AbsCallbackStrategy.class);
        return callbackStrategy.doPayCallbackHandler(request);
    }

    /**
     * 退款信息回调处理
     */
    public String refundHandle(HttpServletRequest request, String channel){
        var callbackStrategy = PaymentStrategyFactory.create(channel, AbsCallbackStrategy.class);
        return callbackStrategy.doRefundCallbackHandler(request);
    }
}
