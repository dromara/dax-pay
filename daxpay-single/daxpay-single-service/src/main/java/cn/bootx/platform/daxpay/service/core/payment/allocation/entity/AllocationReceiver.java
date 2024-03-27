package cn.bootx.platform.daxpay.service.core.payment.allocation.entity;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.code.AllocationRelationTypeEnum;
import cn.bootx.table.modify.annotation.DbColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分账接收方
 * @author xxm
 * @since 2024/3/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AllocationReceiver extends MpBaseEntity {

    @DbColumn(name = "账号别名")
    private String name;

    @DbColumn(name = "所属通道")
    private String channel;

    /**
     * 分账接收方类型 个人/商户
     */
    @DbColumn(name = "分账接收方类型")
    private String receiverType;


    @DbColumn(name = "接收方账号")
    private String receiverAccount;

    /** 接收方姓名 */
    @DbColumn(name = "接收方姓名")
    private String receiverName;

    /**
     * 分账关系类型
     * @see AllocationRelationTypeEnum
     */
    @DbColumn(name = "分账关系类型")
    private String relationType;

    @DbColumn(name = "关系名称")
    private String relationName;

}
