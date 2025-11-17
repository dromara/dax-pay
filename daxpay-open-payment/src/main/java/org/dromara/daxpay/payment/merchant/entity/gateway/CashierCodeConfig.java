package org.dromara.daxpay.payment.merchant.entity.gateway;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.merchant.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.payment.merchant.convert.gateway.CashierCodeConfigConvert;
import org.dromara.daxpay.payment.merchant.result.gateway.CashierCodeConfigResult;
import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import org.dromara.daxpay.payment.pay.enums.PayMethodEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * 收银码牌配置
 * @author xxm
 * @since 2024/11/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("pay_cashier_code_config")
public class CashierCodeConfig extends MchAppBaseEntity implements ToResult<CashierCodeConfigResult> {

    /** 限制用户支付方式 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String limitPay;

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

    /**
     * 转换
     */
    @Override
    public CashierCodeConfigResult toResult() {
        return CashierCodeConfigConvert.CONVERT.toResult(this);
    }

}
