package cn.daxpay.open.payment.merchant.entity.route.strategy;

import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.payment.merchant.convert.route.strategy.PayRouteStrategyConvert;
import cn.daxpay.open.payment.merchant.result.route.strategy.PayRouteStrategyResult;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.core.enums.pay.route.PayRouteModeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class PayRouteStrategy extends MchBaseEntity implements ToResult<PayRouteStrategyResult> {

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String appId;

    /// 路由模式：basic / scene
    /// @see PayRouteModeEnum
    private String mode;

    /// 支付渠道（微信/支付宝/银联）
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum
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
