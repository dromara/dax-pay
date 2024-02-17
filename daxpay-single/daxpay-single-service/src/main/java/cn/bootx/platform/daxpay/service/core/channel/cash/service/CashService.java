package cn.bootx.platform.daxpay.service.core.channel.cash.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 现金支付
 *
 * @author xxm
 * @since 2021/6/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CashService {

    /**
     * 退款
     */
    public void refund(Long paymentId, int amount) {
    }

}
