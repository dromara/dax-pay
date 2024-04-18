package cn.bootx.platform.daxpay.service.core.payment.allocation.entity;

import cn.bootx.platform.common.core.annotation.EncryptionField;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.code.AllocationReceiverTypeEnum;
import cn.bootx.platform.daxpay.code.AllocationRelationTypeEnum;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.core.payment.allocation.convert.AllocationReceiverConvert;
import cn.bootx.platform.daxpay.service.dto.allocation.AllocationReceiverDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
@DbTable(comment = "分账接收方")
@TableName("pay_allocation_receiver")
public class AllocationReceiver extends MpBaseEntity implements EntityBaseFunction<AllocationReceiverDto> {

    @DbColumn(comment = "账号别名")
    private String name;

    /**
     * @see PayChannelEnum
     */
    @DbColumn(comment = "所属通道")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channel;

    /**
     * 分账接收方类型
     * @see AllocationReceiverTypeEnum
     */
    @DbColumn(comment = "分账接收方类型")
    private String receiverType;


    @DbColumn(comment = "接收方账号")
    @EncryptionField
    private String receiverAccount;

    /** 接收方姓名 */
    @DbColumn(comment = "接收方姓名")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String receiverName;

    /**
     * 分账关系类型
     * @see AllocationRelationTypeEnum
     */
    @DbColumn(comment = "分账关系类型")
    private String relationType;

    @DbColumn(comment = "关系名称")
    private String relationName;

    @DbColumn(comment = "是否已经同步到网关")
    private Boolean sync;

    @DbColumn(comment = "备注")
    private String remark;

    /**
     * 转换
     */
    @Override
    public AllocationReceiverDto toDto() {
        return AllocationReceiverConvert.CONVERT.convert(this);
    }
}
