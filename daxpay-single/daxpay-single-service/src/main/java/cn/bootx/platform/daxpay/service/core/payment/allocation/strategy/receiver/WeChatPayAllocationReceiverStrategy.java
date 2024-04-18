package cn.bootx.platform.daxpay.service.core.payment.allocation.strategy.receiver;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPayAllocationReceiverService;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPayConfigService;
import cn.bootx.platform.daxpay.service.func.AbsAllocationReceiverStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信分账接收策略
 * @author xxm
 * @since 2024/4/1
 */
@Slf4j
@Service
@Scope(SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class WeChatPayAllocationReceiverStrategy extends AbsAllocationReceiverStrategy {

    private final WeChatPayAllocationReceiverService receiverService;

    private final WeChatPayConfigService payConfigService;

    private WeChatPayConfig weChatPayConfig;

    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.WECHAT;
    }

    /**
     * 校验方法
     */
    @Override
    public boolean validation(){
        return receiverService.validation(this.getAllocationReceiver());
    }

    /**
     * 分账前处理
     */
    @Override
    public void doBeforeHandler() {
        this.weChatPayConfig = payConfigService.getAndCheckConfig();
        // 判断是否支持分账
        if (Objects.equals(weChatPayConfig.getAllocation(),false)){
            throw new PayFailureException("微信支付配置不支持分账");
        }
    }

    /**
     * 添加到支付系统中
     */
    @Override
    public void bind() {
        if (!receiverService.validation(this.getAllocationReceiver())){
            throw new PayFailureException("分账接收者参数未通过校验");
        }
        receiverService.bind(this.getAllocationReceiver(),this.weChatPayConfig);
    }

    /**
     * 从三方支付系统中删除
     */
    @Override
    public void unbind() {
        if (!receiverService.validation(this.getAllocationReceiver())){
            throw new PayFailureException("分账参数未通过校验");
        }
        receiverService.unbind(this.getAllocationReceiver(),this.weChatPayConfig);
    }
}
