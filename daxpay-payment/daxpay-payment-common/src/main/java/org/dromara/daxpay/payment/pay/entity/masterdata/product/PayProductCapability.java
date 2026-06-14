package org.dromara.daxpay.payment.pay.entity.masterdata.product;

import org.dromara.daxpay.platform.common.mybatisplus.base.MpBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付产品与支付能力关联
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_product_capability")
public class PayProductCapability extends MpBaseEntity {

    /// 支付产品编码
    private String productCode;

    /// 支付能力编码
    private String capabilityCode;

    /// 排序
    private Integer sortNo;
}