package cn.bootx.platform.common.mybatisplus.base;

import cn.bootx.table.modify.annotation.DbColumn;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

/**
 * MP基础类, 真实删除
 *
 * @author xxm
 * @since 2022/7/17
 */
@Getter
@Setter
@FieldNameConstants(innerTypeName = "Del")
public abstract class MpDelEntity extends MpCreateEntity {

    /** 最后修者ID */
    @DbColumn(comment = "最后修者ID", length = 20, order = Integer.MAX_VALUE - 400)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long lastModifier;

    /** 最后修改时间 */
    @DbColumn(comment = "最后修改时间", order = Integer.MAX_VALUE - 300)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastModifiedTime;

    /** 乐观锁 */
    @Version
    @DbColumn(comment = "乐观锁", isNull = false, order = Integer.MAX_VALUE - 200)
    private Integer version = 0;

}
