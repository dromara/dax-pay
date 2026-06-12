package org.dromara.daxpay.payment.merchant.entity.route.strategy;

import org.dromara.daxpay.payment.common.entity.merchant.MchAppBaseEntity;
import org.dromara.daxpay.payment.merchant.convert.route.strategy.PayRouteStrategyConvert;
import org.dromara.daxpay.payment.merchant.result.route.strategy.PayRouteStrategyResult;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.core.enums.pay.route.PayRouteModeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付通道路由策略
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_route_strategy")
public class PayRouteStrategy extends MchAppBaseEntity implements ToResult<PayRouteStrategyResult> {

    /// 路由模式：basic / scene；advanced 为预留值，不可设为生效模式
    /// @see PayRouteModeEnum
    private String mode;

    /// 支付渠道（微信/支付宝/银联）
    /// @see org.dromara.daxpay.platform.core.enums.pay.channel.PayProviderEnum
    private String provider;

    /// 是否启用
    private boolean enable;

    /// 策略名称
    private String name;

    @Override
    public PayRouteStrategyResult toResult() {
        return PayRouteStrategyConvert.CONVERT.toResult(this);
    }
}
