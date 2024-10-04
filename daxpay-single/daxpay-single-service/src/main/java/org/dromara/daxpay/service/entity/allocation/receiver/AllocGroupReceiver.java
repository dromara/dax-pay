package org.dromara.daxpay.service.entity.allocation.receiver;

import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 分账组和接收者关系
 * @author xxm
 * @since 2024/6/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AllocGroupReceiver extends MchAppBaseEntity {

    /** 分账组ID */
    private Long groupId;

    /** 接收者ID */
    private Long receiverId;

    /** 分账比例(百分之多少) */
    private BigDecimal rate;
}
