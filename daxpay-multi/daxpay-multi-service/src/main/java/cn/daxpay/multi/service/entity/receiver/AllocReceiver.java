package cn.daxpay.multi.service.entity.receiver;

import cn.daxpay.multi.core.enums.AllocReceiverTypeEnum;
import cn.daxpay.multi.core.enums.AllocRelationTypeEnum;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.service.common.entity.MchEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分账接收方
 * @author xxm
 * @since 2024/6/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AllocReceiver extends MchEntity {

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
}
