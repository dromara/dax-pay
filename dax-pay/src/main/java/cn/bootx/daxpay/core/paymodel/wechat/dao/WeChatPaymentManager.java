package cn.bootx.daxpay.core.paymodel.wechat.dao;

import cn.bootx.common.mybatisplus.impl.BaseManager;
import cn.bootx.daxpay.core.paymodel.wechat.entity.WeChatPayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 微信支付记录
 *
 * @author xxm
 * @date 2021/6/21
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class WeChatPaymentManager extends BaseManager<WeChatPaymentMapper, WeChatPayment> {

    public Optional<WeChatPayment> findByPaymentId(Long paymentId) {
        return findByField(WeChatPayment::getPaymentId, paymentId);
    }

}
