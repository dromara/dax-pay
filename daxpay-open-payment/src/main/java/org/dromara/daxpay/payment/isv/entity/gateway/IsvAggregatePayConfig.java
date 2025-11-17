package org.dromara.daxpay.payment.isv.entity.gateway;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.isv.convert.gateway.IsvAggregatePayConfigConvert;
import org.dromara.daxpay.payment.isv.param.gateway.IsvAggregatePayConfigParam;
import org.dromara.daxpay.payment.isv.result.gateway.IsvAggregatePayConfigResult;
import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import org.dromara.daxpay.payment.pay.enums.PayMethodEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * 网关聚合支付配置
 * @author xxm
 * @since 2025/3/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_isv_aggregate_qr_pay_config")
public class IsvAggregatePayConfig extends MpBaseEntity implements ToResult<IsvAggregatePayConfigResult> {

    /** 自动拉起支付 */
    private boolean autoLaunch;

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
     * 服务商号
     */
    private String isvNo;

    public Boolean getAutoLaunch() {
        return Objects.equals(autoLaunch,true);
    }

    /**
     * 初始化
     */
    public static IsvAggregatePayConfig init(IsvAggregatePayConfigParam param) {
        return IsvAggregatePayConfigConvert.CONVERT.toEntity(param);
    }

    /**
     * 转换
     */
    @Override
    public IsvAggregatePayConfigResult toResult() {
        return IsvAggregatePayConfigConvert.CONVERT.toResult(this);
    }
}
