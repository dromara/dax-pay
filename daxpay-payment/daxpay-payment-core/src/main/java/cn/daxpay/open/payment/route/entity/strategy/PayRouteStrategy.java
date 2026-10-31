package cn.daxpay.open.payment.route.entity.strategy;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.route.convert.strategy.PayRouteStrategyConvert;
import cn.daxpay.open.payment.route.result.strategy.PayRouteStrategyResult;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.core.enums.pay.route.PayRouteModeEnum;
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

    /// 应用号(创建后不可变更；不参与自动填充, 由业务层显式赋值)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /// 路由模式：basic / scene
    /// @see PayRouteModeEnum
    private String mode;

    @Override
    public PayRouteStrategyResult toResult() {
        return PayRouteStrategyConvert.CONVERT.toResult(this);
    }
}
