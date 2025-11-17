package org.dromara.daxpay.payment.merchant.entity.gateway;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.merchant.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.payment.merchant.convert.gateway.AggregatePayConfigConvert;
import org.dromara.daxpay.payment.merchant.param.gateway.AggregateQrPayConfigParam;
import org.dromara.daxpay.payment.merchant.result.gateway.AggregateQrPayConfigResult;
import org.dromara.daxpay.payment.pay.enums.PayMethodEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 网关聚合扫码支付配置
 * @author xxm
 * @since 2025/3/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_aggregate_qr_pay_config")
public class AggregateQrPayConfig extends MchAppBaseEntity implements ToResult<AggregateQrPayConfigResult> {

    /**
     * 微信场景对应通道
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

    /** 自动拉起支付 */
    private boolean autoLaunch;

    /**
     * 初始化
     */
    public static AggregateQrPayConfig init(AggregateQrPayConfigParam param){
        return AggregatePayConfigConvert.CONVERT.toEntity(param);
    }

    /**
     * 转换
     */
    @Override
    public AggregateQrPayConfigResult toResult() {
        return AggregatePayConfigConvert.CONVERT.toResult(this);
    }
}
