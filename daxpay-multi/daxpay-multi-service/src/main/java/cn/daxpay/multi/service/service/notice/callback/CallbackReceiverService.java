package cn.daxpay.multi.service.service.notice.callback;

import cn.daxpay.multi.service.service.record.callback.CallbackRecordService;
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

    private final CallbackRecordService callbackRecordService;

    private final PayCallbackService payCallbackService;

    private final RefundCallbackService refundCallbackService;

    /**
     * 支付信息回调处理
     */
    public String payHandle(HttpServletRequest request, String channel){
        var callbackStrategy = PaymentStrategyFactory.create(channel, AbsCallbackStrategy.class);
        // 执行回调数据解析, 返回响应对象
        String msg = callbackStrategy.doPayCallbackHandler(request);
        // 执行回调业务处理
        payCallbackService.payCallback();
        // 保存记录
        callbackRecordService.saveCallbackRecord();
        return msg;
    }

    /**
     * 退款信息回调处理
     */
    public String refundHandle(HttpServletRequest request, String channel){
        var callbackStrategy = PaymentStrategyFactory.create(channel, AbsCallbackStrategy.class);
        // 执行回调数据解析, 返回响应对象
        String msg = callbackStrategy.doRefundCallbackHandler(request);
        // 执行回调业务处理
        refundCallbackService.refundCallback();
        // 记录保存
        callbackRecordService.saveCallbackRecord();
        return msg;
    }
}
