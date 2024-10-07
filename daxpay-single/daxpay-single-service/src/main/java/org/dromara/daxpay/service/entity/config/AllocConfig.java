package org.dromara.daxpay.service.entity.config;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;

import java.math.BigDecimal;

/**
 * 分账配置
 * @author xxm
 * @since 2024/10/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_alloc_config")
public class AllocConfig extends MchAppBaseEntity {

    /** 是否自动分账 */
    private Boolean autoAlloc;

    /** 开启分账最低额 */
    private Boolean enableAllocLimit;

    /** 大于多少开启分账 */
    private BigDecimal minAmount;

    /** 分账时机类型 立即/定期/延时 */
    private String allocTimeType;
}
