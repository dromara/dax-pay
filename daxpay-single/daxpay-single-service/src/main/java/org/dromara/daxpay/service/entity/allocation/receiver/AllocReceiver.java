package org.dromara.daxpay.service.entity.allocation.receiver;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.core.enums.AllocRelationTypeEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.service.convert.allocation.AllocReceiverConvert;
import org.dromara.daxpay.service.bo.allocation.AllocReceiverResultBo;

/**
 * 分账接收方
 * @author xxm
 * @since 2024/6/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_alloc_receiver")
public class AllocReceiver extends MchAppBaseEntity implements ToResult<AllocReceiverResultBo> {

    /** 分账接收方编号, 需要保证唯一 */
    private String receiverNo;

    /**
     * 所属通道
     * @see ChannelEnum
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channel;

    /**
     * 分账接收方类型
     * @see AllocReceiverTypeEnum
     */
    private String receiverType;

    /** 接收方账号 */
    private String receiverAccount;

    /** 接收方姓名 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String receiverName;

    /**
     * 分账关系类型
     * @see AllocRelationTypeEnum
     */
    private String relationType;

    /** 关系名称 */
    private String relationName;

    @Override
    public AllocReceiverResultBo toResult() {
        return AllocReceiverConvert.CONVERT.toBo(this);
    }
}
