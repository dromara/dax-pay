package org.dromara.daxpay.channel.alipay.strategy.merchant;

import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import org.dromara.daxpay.payment.pay.enums.PayMethodEnum;
import org.dromara.daxpay.payment.pay.strategy.AbsChannelBasicStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.dromara.daxpay.payment.pay.enums.PayMethodEnum.*;

/**
 *
 * @author xxm
 * @since 2025/6/4
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayChannelBasicStrategy extends AbsChannelBasicStrategy {
    /**
     * 获取通道的支付列表
     */
    @Override
    public List<PayMethodEnum> payMethods() {
        return List.of(ALIPAY_QR, BARCODE, ALIPAY_JSAPI, ALIPAY_PC, ALIPAY_H5, ALIPAY_APP);
    }

    /**
     * 策略标识, 可以自行进行扩展
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.ALIPAY.getCode();
    }
}
