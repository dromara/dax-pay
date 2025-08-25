package org.dromara.daxpay.service.pay.common.entity;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * 商户基础实体类
 * @author xxm
 * @since 2024/6/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@FieldNameConstants
public class MchAppBaseEntity extends MpBaseEntity {

    /** 商户号 */
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String mchNo;

    /** 应用号 */
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String appId;

}
