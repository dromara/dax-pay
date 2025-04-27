package org.dromara.daxpay.service.entity.allocation.receiver;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.allocation.AllocGroupConvert;
import org.dromara.daxpay.service.result.allocation.receiver.AllocGroupVo;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 分账组
 * @author xxm
 * @since 2024/6/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_alloc_group")
public class AllocGroup extends MchAppBaseEntity implements ToResult<AllocGroupVo> {

    /** 分账组编码 */
    private String groupNo;

    /** 名称 */
    private String name;

    /** 通道 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channel;

    /** 是否为默认分账组 */
    private boolean defaultGroup;

    /** 总分账比例(百分之多少) */
    private BigDecimal totalRate;

    /** 备注 */
    @Deprecated
    private String remark;

    @Override
    public AllocGroupVo toResult() {
        return AllocGroupConvert.CONVERT.toVo(this);
    }
}
