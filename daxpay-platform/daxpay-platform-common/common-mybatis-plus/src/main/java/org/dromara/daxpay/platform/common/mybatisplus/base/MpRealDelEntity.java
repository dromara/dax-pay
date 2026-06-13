package org.dromara.daxpay.platform.common.mybatisplus.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.OffsetDateTime;

/// # MP基础类,真实删除
///
@Getter
@Setter
@FieldNameConstants(innerTypeName = "RealDel")
public abstract class MpRealDelEntity extends MpCreateEntity {

    /// 最后修改ID
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long lastModifier;

    /// 最后修改时间 (UTC)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private OffsetDateTime lastModifiedTime;

    /// 版本号, 使用乐观锁
    @Version
    private Integer version = 0;

}
