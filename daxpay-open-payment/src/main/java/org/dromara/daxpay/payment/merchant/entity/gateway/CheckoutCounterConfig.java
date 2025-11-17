package org.dromara.daxpay.payment.merchant.entity.gateway;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.merchant.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.payment.merchant.convert.gateway.CheckoutCounterConfigConvert;
import org.dromara.daxpay.payment.merchant.param.gateway.CheckoutCounterConfigParam;
import org.dromara.daxpay.payment.merchant.result.gateway.CheckoutCounterConfigResult;
import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import org.dromara.daxpay.payment.pay.enums.PayMethodEnum;
import org.dromara.daxpay.payment.unipay.enums.CheckoutCounterTypeEnum;
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
@TableName(value = "pay_checkout_counter_config",autoResultMap = true)
public class CheckoutCounterConfig extends MchAppBaseEntity implements ToResult<CheckoutCounterConfigResult> {

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
    private String bgColor;

    /** 边框色 */
    private String borderColor;

    /** 字体颜色 */
    private String fontColor;

    /** 图标 */
    private String icon;

    /** 排序 */
    private Double sortNo;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    private String channel;

    /**
     * 支付方式
     * @see PayMethodEnum
     */
    private String payMethod;

    /**
     * 构造
     */
    public static CheckoutCounterConfig init(CheckoutCounterConfigParam param) {
        return CheckoutCounterConfigConvert.CONVERT.toEntity(param);
    }

    /**
     * 转换
     */
    @Override
    public CheckoutCounterConfigResult toResult() {
        return CheckoutCounterConfigConvert.CONVERT.toResult(this);
    }
}
