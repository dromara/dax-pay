package cn.daxpay.single.service.core.payment.allocation.strategy.allocation;

import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.exception.pay.PayFailureException;
import cn.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import cn.daxpay.single.service.core.channel.alipay.service.AliPayAllocationService;
import cn.daxpay.single.service.core.channel.alipay.service.AliPayConfigService;
import cn.daxpay.single.service.core.payment.sync.result.AllocRemoteSyncResult;
import cn.daxpay.single.service.func.AbsAllocationStrategy;
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
    public String getChannel() {
        return PayChannelEnum.ALI.getCode();
    }

    /**
     * 操作前处理, 校验和初始化支付配置
     */
    @Override
    public void doBeforeHandler() {
        this.aliPayConfig = aliPayConfigService.getConfig();
        // 判断是否支持分账
        if (Objects.equals(aliPayConfig.getAllocation(),false)){
            throw new PayFailureException("支付宝支付配置不支持分账");
        }
        aliPayConfigService.initConfig(this.aliPayConfig);
    }

    /**
     * 分账操作
     */
    @Override
    public void allocation() {
        aliPayAllocationService.allocation(this.getAllocOrder(), this.getAllocOrderDetails());
    }

    /**
     * 分账完结
     */
    @Override
    public void finish() {
        aliPayAllocationService.finish(this.getAllocOrder(), this.getAllocOrderDetails());
    }

    /**
     * 同步状态
     */
    @Override
    public AllocRemoteSyncResult doSync() {
        return aliPayAllocationService.sync(this.getAllocOrder(), this.getAllocOrderDetails());
    }

}
