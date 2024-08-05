package cn.daxpay.multi.service.entity.allocation.receiver;

import cn.daxpay.multi.service.common.entity.MchBaseEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class AllocGroup extends MchBaseEntity {

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
    private String remark;
}
