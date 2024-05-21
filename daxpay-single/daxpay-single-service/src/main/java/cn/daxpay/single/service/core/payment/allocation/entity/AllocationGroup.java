package cn.daxpay.single.service.core.payment.allocation.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.single.service.core.payment.allocation.convert.AllocationGroupConvert;
import cn.daxpay.single.service.dto.allocation.AllocationGroupDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbComment;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分账组
 * @author xxm
 * @since 2024/3/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "分账组")
@TableName("pay_allocation_group")
public class AllocationGroup extends MpBaseEntity implements EntityBaseFunction<AllocationGroupDto> {

    /** 分账组编码 */
    @DbColumn(comment = "分账组编码")
    private String groupNo;

    /** 名称 */
    @DbComment("名称")
    private String name;

    /** 通道 */
    @DbColumn(comment = "通道")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channel;

    /** 是否为默认分账组 */
    @DbColumn(comment = "默认分账组")
    private boolean defaultGroup;

    /** 总分账比例(万分之多少) */
    @DbColumn(comment = "总分账比例(万分之多少)")
    private Integer totalRate;

    /** 备注 */
    @DbColumn(comment = "备注")
    private String remark;

    /**
     * 转换
     */
    @Override
    public AllocationGroupDto toDto() {
        return AllocationGroupConvert.CONVERT.convert(this);
    }
}
