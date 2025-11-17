package org.dromara.daxpay.channel.wechat.strategy.sub;

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
public class WechatSubChannelBasicStrategy extends AbsChannelBasicStrategy {
    /**
     * 获取通道的支付列表
     */
    @Override
    public List<PayMethodEnum> payMethods() {
        return List.of(WECHAT_QR,WECHAT_APP,WECHAT_H5,BARCODE,WECHAT_JSAPI, WECHAT_MINI );
    }

    /**
     * 策略标识, 可以自行进行扩展
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT_ISV.getCode();
    }
}
