package org.dromara.daxpay.payment.merchant.entity.gateway;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.merchant.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.payment.merchant.convert.gateway.AggregatePayConfigConvert;
import org.dromara.daxpay.payment.merchant.result.gateway.AggregateBarPayConfigResult;
import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import org.dromara.daxpay.payment.pay.enums.PayMethodEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 网关聚合付款码支付配置
 * @author xxm
 * @since 2025/3/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_aggregate_bar_pay_config")
public class AggregateBarPayConfig extends MchAppBaseEntity implements ToResult<AggregateBarPayConfigResult> {

    /**
     * 微信场景对应通道
     * @see ChannelEnum
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String wxChannel;

    /**
     * 微信场景对应支付方式
     * @see PayMethodEnum
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String wxMethod;

    /**
     * 支付宝场景对应通道
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String alipayChannel;

    /**
     * 支付宝场景对应支付方式
     * @see PayMethodEnum
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String alipayMethod;

    /**
     * 银联场景对应通道
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String unionChannel;

    /**
     * 银联场景对应支付方式
     * @see PayMethodEnum
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String unionMethod;

    /** 付款终端号 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String terminalNo;

    /**
     * 转换
     */
    @Override
    public AggregateBarPayConfigResult toResult() {
        return AggregatePayConfigConvert.CONVERT.toResult(this);
    }
}
