package org.dromara.daxpay.payment.merchant.common.entity;

import org.dromara.daxpay.payment.isv.common.entity.IsvBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * 商户基础实体类 应用/商户号可以更新
 * @author xxm
 * @since 2025/7/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@FieldNameConstants
public class MchAppEditEntity extends IsvBaseEntity {

    /** 商户号 */
    private String mchNo;

    /** 应用号 */
    private String appId;

}
