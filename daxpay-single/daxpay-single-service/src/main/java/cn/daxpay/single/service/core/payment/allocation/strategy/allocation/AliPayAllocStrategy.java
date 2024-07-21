package cn.daxpay.single.service.core.payment.allocation.strategy.allocation;

import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.core.exception.ConfigNotEnableException;
import cn.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import cn.daxpay.single.service.core.channel.alipay.service.AliPayAllocService;
import cn.daxpay.single.service.core.channel.alipay.service.AliPayConfigService;
import cn.daxpay.single.service.core.payment.sync.result.AllocRemoteSyncResult;
import cn.daxpay.single.service.func.AbsAllocStrategy;
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
public class AliPayAllocStrategy extends AbsAllocStrategy {

    private final AliPayAllocService aliPayAllocService;

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
            throw new ConfigNotEnableException("支付宝支付配置未开启分账");
        }
    }

    /**
     * 分账操作
     */
    @Override
    public void allocation() {
        aliPayAllocService.allocation(this.getAllocOrder(), this.getAllocOrderDetails(), this.aliPayConfig);
    }

    /**
     * 分账完结
     */
    @Override
    public void finish() {
        aliPayAllocService.finish(this.getAllocOrder(), this.getAllocOrderDetails(), this.aliPayConfig);
    }

    /**
     * 同步状态
     */
    @Override
    public AllocRemoteSyncResult doSync() {
        return aliPayAllocService.sync(this.getAllocOrder(), this.getAllocOrderDetails(), this.aliPayConfig);
    }

}
