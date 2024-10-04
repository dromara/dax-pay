package org.dromara.daxpay.service.common.entity;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
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
public class MchAppRecordEntity extends MpCreateEntity {

    /** 应用号 */
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String appId;

}
