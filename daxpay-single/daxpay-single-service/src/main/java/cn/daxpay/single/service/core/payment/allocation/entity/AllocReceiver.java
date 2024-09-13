package cn.daxpay.single.service.core.payment.allocation.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.single.core.code.AllocReceiverTypeEnum;
import cn.daxpay.single.core.code.AllocRelationTypeEnum;
import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.service.common.typehandler.DecryptTypeHandler;
import cn.daxpay.single.service.core.payment.allocation.convert.AllocReceiverConvert;
import cn.daxpay.single.service.dto.allocation.AllocReceiverDto;
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
@TableName(value = "pay_allocation_receiver",autoResultMap = true)
public class AllocReceiver extends MpBaseEntity implements EntityBaseFunction<AllocReceiverDto> {

    /** 分账接收方编号, 需要保证唯一 */
    @DbColumn(comment = "分账接收方编号", length = 32, isNull = false)
    private String receiverNo;

    /**
     * 所属通道
     * @see PayChannelEnum
     */
    @DbColumn(comment = "所属通道", length = 20, isNull = false)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channel;

    /**
     * 分账接收方类型
     * @see AllocReceiverTypeEnum
     */
    @DbColumn(comment = "分账接收方类型", length = 20, isNull = false)
    private String receiverType;

    /** 接收方账号 */
    @DbColumn(comment = "接收方账号", length = 100, isNull = false)
    @TableField(typeHandler = DecryptTypeHandler.class)
    private String receiverAccount;

    /** 接收方姓名 */
    @DbColumn(comment = "接收方姓名", length = 100)
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String receiverName;

    /**
     * 分账关系类型
     * @see AllocRelationTypeEnum
     */
    @DbColumn(comment = "分账关系类型", length = 20, isNull = false)
    private String relationType;

    /** 关系名称 */
    @DbColumn(comment = "关系名称", length = 50)
    private String relationName;

    /**
     * 转换
     */
    @Override
    public AllocReceiverDto toDto() {
        return AllocReceiverConvert.CONVERT.convert(this);
    }
}
