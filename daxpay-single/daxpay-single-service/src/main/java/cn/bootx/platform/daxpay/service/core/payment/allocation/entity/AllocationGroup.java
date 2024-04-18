package cn.bootx.platform.daxpay.service.core.payment.allocation.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.service.core.payment.allocation.convert.AllocationGroupConvert;
import cn.bootx.platform.daxpay.service.dto.allocation.AllocationGroupDto;
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

    @DbComment("名称")
    private String name;

    @DbColumn(comment = "通道")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channel;

    @DbColumn(comment = "默认分账组")
    private boolean defaultGroup;

    @DbColumn(comment = "总分账比例(万分之多少)")
    private Integer totalRate;

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
