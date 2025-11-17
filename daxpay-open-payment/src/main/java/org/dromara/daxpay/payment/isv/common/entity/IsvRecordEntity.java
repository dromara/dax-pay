package org.dromara.daxpay.payment.isv.common.entity;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * 服务商记录实体类, 只有创建人和时间
 * @author xxm
 * @since 2025/5/26
 */
@Getter
@Setter
@FieldNameConstants
public class IsvRecordEntity extends MpCreateEntity {

    /** 服务商号 */
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String isvNo;

}
