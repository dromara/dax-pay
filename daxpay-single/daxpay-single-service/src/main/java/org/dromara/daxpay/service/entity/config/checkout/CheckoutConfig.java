package org.dromara.daxpay.service.entity.config.checkout;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;

/**
 * 收银台配置
 * @author xxm
 * @since 2024/11/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CheckoutConfig extends MchAppBaseEntity {
    /** 收银台名称 */
    private String name;

}
