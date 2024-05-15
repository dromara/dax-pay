package cn.bootx.platform.common.mybatisplus.base;

import cn.bootx.table.modify.annotation.DbColumn;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

/**
 * 基础实体类 (带软删除)
 *
 * @author xxm
 * @since 2021/7/27
 */
@Getter
@Setter
@FieldNameConstants(innerTypeName = "Base")
public abstract class MpBaseEntity extends MpDelEntity implements Serializable {

    private static final long serialVersionUID = -2699324766101179583L;

    /** 删除标志 */
    @TableLogic
    @DbColumn(comment = "删除标志", isNull = false, order = Integer.MAX_VALUE - 100)
    private boolean deleted;

}
