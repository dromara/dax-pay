package org.dromara.daxpay.service.entity.config.checkout;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.config.CheckoutItemConfigConvert;
import org.dromara.daxpay.service.param.config.checkout.CheckoutItemConfigParam;
import org.dromara.daxpay.service.result.config.checkout.CheckoutItemConfigResult;

/**
 * 收银台配置项
 * @author xxm
 * @since 2024/11/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CheckoutItemConfig extends MchAppBaseEntity implements ToResult<CheckoutItemConfigResult> {

    /** 类目配置Id */
    private Long classifyId;

    /** 排序 */
    private Double sort;

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

    /** 是否开启分账 */
    private boolean allocation;

    /**
     * 类型
     * 1. 扫码支付
     * 2. 条码支付
     * 3. 跳转链接
     * 4. 小程序支付
     * 5. 聚合支付
     */
    private String type;

    /**
     * 构造
     */
    public static CheckoutItemConfig init(CheckoutItemConfigParam param) {
        return CheckoutItemConfigConvert.CONVERT.toEntity(param);
    }

    /**
     * 转换
     */
    @Override
    public CheckoutItemConfigResult toResult() {
        return CheckoutItemConfigConvert.CONVERT.toResult(this);
    }
}
