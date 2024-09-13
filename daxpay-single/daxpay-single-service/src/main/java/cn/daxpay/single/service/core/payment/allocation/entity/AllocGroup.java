package cn.daxpay.single.service.core.payment.allocation.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.daxpay.single.service.core.payment.allocation.convert.AllocGroupConvert;
import cn.daxpay.single.service.dto.allocation.AllocGroupDto;
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
public class AllocGroup extends MpBaseEntity implements EntityBaseFunction<AllocGroupDto> {

    /** 分账组编码 */
    @DbColumn(comment = "分账组编码", length = 20, isNull = false)
    private String groupNo;

    /** 名称 */
    @DbColumn(comment = "名称", length = 50)
    private String name;

    /** 通道 */
    @DbColumn(comment = "通道", length = 20, isNull = false)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channel;

    /** 是否为默认分账组 */
    @DbColumn(comment = "默认分账组")
    private boolean defaultGroup;

    /** 总分账比例(万分之多少) */
    @DbColumn(comment = "总分账比例(万分之多少)", length = 5)
    private Integer totalRate;

    /** 备注 */
    @DbColumn(comment = "备注", length = 200)
    private String remark;

    /**
     * 转换
     */
    @Override
    public AllocGroupDto toDto() {
        return AllocGroupConvert.CONVERT.convert(this);
    }
}
