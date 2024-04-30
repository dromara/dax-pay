package cn.bootx.platform.common.mybatisplus.base;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * 基础实体类 (逻辑删除)
 *
 * @author xxm
 * @since 2021/7/27
 */
@Getter
@Setter
@FieldNameConstants(innerTypeName = "Base")
public abstract class MpBaseEntity extends MpRealDelEntity{

    /** 删除标志 */
    @TableLogic
    private boolean deleted;

}
