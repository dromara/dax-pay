package org.dromara.daxpay.payment.isv.entity.gateway;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.isv.convert.gateway.IsvCheckoutCounterConfigConvert;
import org.dromara.daxpay.payment.isv.param.gateway.IsvCheckoutCounterConfigParam;
import org.dromara.daxpay.payment.isv.result.gateway.IsvCheckoutCounterConfigResult;
import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import org.dromara.daxpay.payment.pay.enums.PayMethodEnum;
import org.dromara.daxpay.payment.unipay.enums.CheckoutCounterTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 网关收银台配置项
 * @author xxm
 * @since 2024/11/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_isv_checkout_counter_config",autoResultMap = true)
public class IsvCheckoutCounterConfig extends MpBaseEntity implements ToResult<IsvCheckoutCounterConfigResult> {

    /** 名称 */
    private String name;

    /**
     * 类型
     * @see CheckoutCounterTypeEnum
     */
    private String type;

    /** 是否推荐 */
    private boolean recommend;

    /** 背景色 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String bgColor;

    /** 边框色 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String borderColor;

    /** 字体颜色 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String fontColor;

    /** 图标 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String icon;

    /** 排序 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Double sortNo;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String channel;

    /**
     * 支付方式
     * @see PayMethodEnum
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String payMethod;

    /**
     * 服务商号
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String isvNo;

    /**
     * 构造
     */
    public static IsvCheckoutCounterConfig init(IsvCheckoutCounterConfigParam param) {
        return IsvCheckoutCounterConfigConvert.CONVERT.toEntity(param);
    }

    /**
     * 转换
     */
    @Override
    public IsvCheckoutCounterConfigResult toResult() {
        return IsvCheckoutCounterConfigConvert.CONVERT.toResult(this);
    }
}
