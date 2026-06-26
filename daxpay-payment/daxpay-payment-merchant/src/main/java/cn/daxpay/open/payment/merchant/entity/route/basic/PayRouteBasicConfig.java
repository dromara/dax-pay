package cn.daxpay.open.payment.merchant.entity.route.basic;

import cn.daxpay.open.payment.merchant.convert.route.basic.PayRouteBasicConfigConvert;
import cn.daxpay.open.payment.merchant.result.route.basic.PayRouteBasicConfigResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付通道路由基础模式配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_route_basic_config")
public class PayRouteBasicConfig extends MpBaseEntity implements ToResult<PayRouteBasicConfigResult> {

    /// 路由策略ID
    private Long strategyId;

    /// 支付渠道（微信/支付宝/银联）
    /// @see PayProviderEnum
    private String provider;

    /// 通道商户号(唯一绑定一个支付产品，替代旧版 product 字段)
    private String channelMchNo;

    @Override
    public PayRouteBasicConfigResult toResult() {
        return PayRouteBasicConfigConvert.CONVERT.toResult(this);
    }
}
