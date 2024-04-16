package cn.bootx.platform.daxpay.service.core.payment.allocation.strategy.allocation;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayAllocationService;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayConfigService;
import cn.bootx.platform.daxpay.service.func.AbsAllocationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝分账策略
 * @author xxm
 * @since 2024/4/1
 */
@Slf4j
@Service
@Scope(SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class AliPayAllocationStrategy extends AbsAllocationStrategy {

    private final AliPayAllocationService aliPayAllocationService;

    private final AliPayConfigService aliPayConfigService;

    private AliPayConfig aliPayConfig;;


    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.ALI;
    }

    /**
     * 操作前处理, 校验和初始化支付配置
     */
    @Override
    public void doBeforeHandler() {
        this.aliPayConfig = aliPayConfigService.getConfig();
        // 判断是否支持分账
        if (Objects.equals(aliPayConfig.getAllocation(),false)){
            throw new PayFailureException("微信支付配置不支持分账");
        }
        aliPayConfigService.initConfig(this.aliPayConfig);
    }

    /**
     * 分账操作
     */
    @Override
    public void allocation() {
        aliPayAllocationService.allocation(this.getAllocationOrder(), this.getAllocationOrderDetails());
    }

    /**
     * 分账完结
     */
    @Override
    public void finish() {
        aliPayAllocationService.finish(this.getAllocationOrder(), this.getAllocationOrderDetails());
    }

    /**
     * 同步状态
     */
    @Override
    public void doSync() {
        aliPayAllocationService.sync(this.getAllocationOrder(), this.getAllocationOrderDetails());
    }

}
