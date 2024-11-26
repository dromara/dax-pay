package org.dromara.daxpay.service.entity.config.checkout;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;

/**
 * 收银台类目配置
 * @author xxm
 * @since 2024/11/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CheckoutGroupConfig extends MchAppBaseEntity {

    /** 类型 web/h5/小程序 */
    private String type;

    /** 名称 */
    private String name;

    /** 排序 */
    private Double sort;
}
