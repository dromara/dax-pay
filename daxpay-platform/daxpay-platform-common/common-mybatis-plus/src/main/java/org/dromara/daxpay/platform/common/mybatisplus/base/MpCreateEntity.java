package org.dromara.daxpay.platform.common.mybatisplus.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.OffsetDateTime;

/// # 创建实体类 (不带逻辑删除)
///
@Getter
@Setter
@FieldNameConstants(innerTypeName = "Create")
public abstract class MpCreateEntity extends MpIdEntity {

    /// 创建者ID
    @TableField(fill = FieldFill.INSERT)
    private Long creator;

    /// 创建时间 (UTC)
    @TableField(fill = FieldFill.INSERT)
    private OffsetDateTime createTime;

}
