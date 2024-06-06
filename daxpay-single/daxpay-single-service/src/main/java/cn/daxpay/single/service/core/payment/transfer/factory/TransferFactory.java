package cn.daxpay.single.service.core.payment.transfer.factory;

import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.exception.pay.PayUnsupportedMethodException;
import cn.daxpay.single.service.core.payment.transfer.strategy.AliPayTransferStrategy;
import cn.daxpay.single.service.func.AbsTransferStrategy;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;

/**
 * 转账工具类
 * @author xxm
 * @since 2024/3/21
 */
@UtilityClass
public class TransferFactory {

    /**
     * 获取转账策略
     * @param channelCode 支付通道编码
     * @return 支付同步策略类
     */
    public AbsTransferStrategy create(String channelCode) {
        AbsTransferStrategy strategy;
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(channelCode);
        switch (channelEnum) {
            case ALI:
                strategy = SpringUtil.getBean(AliPayTransferStrategy.class);
                break;
            default:
                throw new PayUnsupportedMethodException();
        }
        return strategy;
    }
}
