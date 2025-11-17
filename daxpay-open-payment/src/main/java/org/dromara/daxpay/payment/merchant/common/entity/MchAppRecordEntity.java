package org.dromara.daxpay.payment.merchant.common.entity;

import org.dromara.daxpay.payment.isv.common.entity.IsvRecordEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * 商户记录实体类, 只有创建人和时间
 * @author xxm
 * @since 2024/7/21
 */
@Getter
@Setter
@FieldNameConstants(innerTypeName = "MchRecord")
public class MchAppRecordEntity extends IsvRecordEntity {

    /** 商户号 */
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String mchNo;

    /** 应用号 */
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String appId;

}
