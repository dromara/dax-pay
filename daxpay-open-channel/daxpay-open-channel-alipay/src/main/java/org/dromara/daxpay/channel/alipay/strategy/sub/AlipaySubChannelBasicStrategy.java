package org.dromara.daxpay.channel.alipay.strategy.sub;

import cn.bootx.platform.core.rest.dto.KeyValue;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.pay.strategy.AbsChannelBasicStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

import static org.dromara.daxpay.core.enums.PayMethodEnum.*;

/**
 *
 * @author xxm
 * @since 2025/6/4
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipaySubChannelBasicStrategy extends AbsChannelBasicStrategy {
    /**
     * 获取通道的支付列表
     */
    @Override
    public List<KeyValue> payMethodList() {
        return Stream.of(WAP,WEB,APP,QRCODE,BARCODE,JSAPI )
                .map(payMethodEnum -> new KeyValue(payMethodEnum.getCode(), payMethodEnum.getName()))
                .toList();
    }

    /**
     * 策略标识, 可以自行进行扩展
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.ALIPAY_ISV.getCode();
    }
}
