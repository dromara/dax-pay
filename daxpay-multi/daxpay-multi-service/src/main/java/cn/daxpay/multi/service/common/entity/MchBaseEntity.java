package cn.daxpay.multi.service.common.entity;

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
@FieldNameConstants(innerTypeName = "MchBase")
public class MchBaseEntity extends MpBaseEntity {

    /** 商户号 */
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String mchNo;

    /** 应用号 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

}
