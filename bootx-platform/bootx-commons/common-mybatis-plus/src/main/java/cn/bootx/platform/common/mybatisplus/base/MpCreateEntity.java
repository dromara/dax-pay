package cn.bootx.platform.common.mybatisplus.base;

import cn.bootx.table.modify.annotation.DbColumn;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

/**
 * 创建实体类 (不带逻辑删除)
 * @author xxm
 * @since 2022/7/26
 */
@Getter
@Setter
@FieldNameConstants(innerTypeName = "Create")
public abstract class MpCreateEntity extends MpIdEntity {

    /** 创建者ID */
    @DbColumn(comment = "创建者ID", length = 20, order = Integer.MAX_VALUE - 600)
    @TableField(fill = FieldFill.INSERT)
    private Long creator;

    /** 创建时间 */
    @DbColumn(comment = "创建时间", order = Integer.MAX_VALUE - 500)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
