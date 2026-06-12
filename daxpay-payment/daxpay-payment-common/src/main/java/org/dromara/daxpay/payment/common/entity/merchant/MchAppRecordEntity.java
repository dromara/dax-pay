package org.dromara.daxpay.payment.common.entity.merchant;

import org.dromara.daxpay.platform.common.mybatisplus.base.MpCreateEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/// # 商户记录实体类, 只有创建人和时间
///
@Getter
@Setter
@FieldNameConstants(innerTypeName = "MchRecord")
public class MchAppRecordEntity extends MpCreateEntity {

    /// 商户号
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String mchNo;

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String appId;

}
