package cn.bootx.platform.daxpay.service.core.payment.reconcile.strategy;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayReconcileService;
import cn.bootx.platform.daxpay.service.func.AbsReconcileStrategy;
import cn.hutool.core.date.DatePattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝对账策略
 * @author xxm
 * @since 2024/1/17
 */
@Slf4j
@Service
@Scope(SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class AlipayReconcileStrategy extends AbsReconcileStrategy {

    private final AliPayReconcileService reconcileService;

    private final AliPayConfigService configService;

    private AliPayConfig config;


    /**
     * 策略标识
     *
     * @see PayChannelEnum
     */
    @Override
    public PayChannelEnum getType() {
        return PayChannelEnum.ALI;
    }

    /**
     * 对账前处理, 主要是初始化支付SDK配置
     */
    @Override
    public void doBeforeHandler() {
        this.config = configService.getConfig();
        configService.initConfig(this.config);
    }

    /**
     * 下载和保存对账单
     */
    @Override
    public void downAndSave() {
        String date = LocalDateTimeUtil.format(this.getRecordOrder().getDate(), DatePattern.NORM_DATE_PATTERN);
        reconcileService.downAndSave(date,this.getRecordOrder().getId());
    }
}
