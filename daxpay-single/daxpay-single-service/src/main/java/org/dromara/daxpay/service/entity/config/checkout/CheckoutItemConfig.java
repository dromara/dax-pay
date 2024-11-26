package org.dromara.daxpay.service.entity.config.checkout;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;

/**
 * 收银台配置项
 * @author xxm
 * @since 2024/11/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CheckoutItemConfig extends MchAppBaseEntity {

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

}
