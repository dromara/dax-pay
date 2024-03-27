package cn.bootx.platform.daxpay.service.core.payment.allocation.entity;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.table.modify.annotation.DbColumn;
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
public class AllocationReceiverGroup extends MpCreateEntity {

    @DbColumn("分账ID")
    private Long groupId;

    @DbColumn("接收者ID")
    private Long receiverId;
}
