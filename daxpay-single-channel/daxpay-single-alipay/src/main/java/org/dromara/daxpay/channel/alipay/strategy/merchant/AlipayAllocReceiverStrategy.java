package org.dromara.daxpay.channel.alipay.strategy.merchant;

import cn.bootx.platform.core.exception.ValidationFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.alipay.entity.AliPayConfig;
import org.dromara.daxpay.channel.alipay.service.allocation.AlipayAllocReceiverService;
import org.dromara.daxpay.channel.alipay.service.config.AlipayConfigService;
import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.strategy.AbsAllocReceiverStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝分账接收者策略
 * @author xxm
 * @since 2024/4/1
 */
@Slf4j
@Service
@Scope(SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class AlipayAllocReceiverStrategy extends AbsAllocReceiverStrategy {

    private final AlipayAllocReceiverService receiverService;

    private final AlipayConfigService aliPayConfigService;

    /**
     * 策略标识
     */
    @Override
    public String getChannel() {
        return ChannelEnum.ALIPAY.getCode();
    }

    @Override
    public List<AllocReceiverTypeEnum> getSupportReceiverTypes() {
        return List.of(AllocReceiverTypeEnum.LOGIN_NAME, AllocReceiverTypeEnum.USER_ID, AllocReceiverTypeEnum.OPEN_ID);
    }

    /**
     * 校验方法
     */
    @Override
    public boolean validation(){
        AliPayConfig aliPayConfig = aliPayConfigService.getAliPayConfig(false);
        return receiverService.validation(this.getAllocReceiver());
    }

    /**
     * 添加到支付系统中
     */
    @Override
    public void bind() {
        AliPayConfig aliPayConfig = aliPayConfigService.getAliPayConfig(false);
        if (!receiverService.validation(this.getAllocReceiver())){
            throw new ValidationFailedException("分账接收者参数未通过校验");
        }
        receiverService.bind(this.getAllocReceiver(),aliPayConfig);
    }

    /**
     * 从三方支付系统中删除
     */
    @Override
    public void unbind() {
        AliPayConfig aliPayConfig = aliPayConfigService.getAliPayConfig(false);
        if (!receiverService.validation(this.getAllocReceiver())){
            throw new ValidationFailedException("分账参数未通过校验");
        }
        receiverService.unbind(this.getAllocReceiver(),aliPayConfig);
    }
}
