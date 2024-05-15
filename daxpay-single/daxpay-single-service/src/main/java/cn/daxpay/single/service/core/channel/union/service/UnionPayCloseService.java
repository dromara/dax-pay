package cn.daxpay.single.service.core.channel.union.service;

import cn.daxpay.single.exception.pay.PayFailureException;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.sdk.union.api.UnionPayKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 云闪付支付关闭
 * @author xxm
 * @since 2024/3/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayCloseService {

    /**
     * 关闭订单
     */
    public void close(PayOrder payOrder, UnionPayKit unionPayKit) {
        throw new PayFailureException("云闪付没有关闭订单功能!");
//        this.verifyErrorMsg(result);
    }
}
