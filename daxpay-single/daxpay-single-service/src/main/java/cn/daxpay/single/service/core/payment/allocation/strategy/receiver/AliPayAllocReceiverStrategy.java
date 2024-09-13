package cn.daxpay.single.service.core.payment.allocation.strategy.receiver;

import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.core.exception.ConfigNotEnableException;
import cn.bootx.platform.common.core.exception.ValidationFailedException;
import cn.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import cn.daxpay.single.service.core.channel.alipay.service.AliPayAllocReceiverService;
import cn.daxpay.single.service.core.channel.alipay.service.AliPayConfigService;
import cn.daxpay.single.service.func.AbsAllocReceiverStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
public class AliPayAllocReceiverStrategy extends AbsAllocReceiverStrategy {

    private final AliPayAllocReceiverService receiverService;

    private final AliPayConfigService payConfigService;

    private AliPayConfig aliPayConfig;

    /**
     * 策略标识
     */
    @Override
    public String getChannel() {
        return PayChannelEnum.ALI.getCode();
    }

    /**
     * 校验方法
     */
    @Override
    public boolean validation(){
        return receiverService.validation(this.getAllocReceiver());
    }

    /**
     * 分账前处理
     */
    @Override
    public void doBeforeHandler() {
        this.aliPayConfig = payConfigService.getAndCheckConfig();
        // 判断是否支持分账
        if (Objects.equals(aliPayConfig.getAllocation(),false)){
            throw new ConfigNotEnableException("微信支付配置未开启分账");
        }
    }

    /**
     * 添加到支付系统中
     */
    @Override
    public void bind() {
        if (!receiverService.validation(this.getAllocReceiver())){
            throw new ValidationFailedException("分账接收者参数未通过校验");
        }
        receiverService.bind(this.getAllocReceiver(), this.aliPayConfig);
    }

    /**
     * 从三方支付系统中删除
     */
    @Override
    public void unbind() {
        if (!receiverService.validation(this.getAllocReceiver())){
            throw new ValidationFailedException("分账参数未通过校验");
        }
        receiverService.unbind(this.getAllocReceiver(), this.aliPayConfig);
    }
}
