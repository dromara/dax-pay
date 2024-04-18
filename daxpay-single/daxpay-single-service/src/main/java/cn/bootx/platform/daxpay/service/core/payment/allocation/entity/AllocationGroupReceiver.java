package cn.bootx.platform.daxpay.service.core.payment.allocation.entity;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分账接收组关系
 * @author xxm
 * @since 2024/3/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "分账接收组关系")
@TableName("pay_allocation_group_receiver")
public class AllocationGroupReceiver extends MpCreateEntity {

    @DbColumn(comment = "分账组ID")
    private Long groupId;

    @DbColumn(comment = "接收者ID")
    private Long receiverId;

    @DbColumn(comment = "分账比例(万分之多少)")
    private Integer rate;
}
