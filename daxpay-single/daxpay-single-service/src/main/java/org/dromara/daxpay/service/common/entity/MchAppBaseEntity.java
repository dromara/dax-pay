package org.dromara.daxpay.service.common.entity;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 *商户基础实体类
 * @author xxm
 * @since 2024/6/1
 */
@Getter
@Setter
@FieldNameConstants(innerTypeName = "MchApp")
public class MchAppBaseEntity extends MpBaseEntity {

    /** 应用号 */
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String appId;

}
