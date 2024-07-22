package cn.daxpay.multi.service.service.payment.callback;

import cn.daxpay.multi.service.strategy.AbsCallbackReceiverStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 回调处理类
 * @author xxm
 * @since 2024/7/19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CallbackReceiverService {
    private final List<AbsCallbackReceiverStrategy> callbackReceiverStrategies;

    public void doCallback(String channel, String orderNo, String outOrderNo) {
        callbackReceiverStrategies.stream().filter(s -> s.getChannel().equals(channel)).findFirst().ifPresent(s -> s.doCallback(orderNo, outOrderNo));
    }
}
