package cn.daxpay.single.sdk.model.allocation;

import cn.daxpay.single.sdk.code.AllocReceiverTypeEnum;
import cn.daxpay.single.sdk.code.AllocRelationTypeEnum;
import cn.daxpay.single.sdk.code.PayChannelEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 分账接收方
 * @author xxm
 * @since 2024/5/21
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AllocReceiverModel {

    /** 分账接收方编号, 需要保证唯一 */
    private String receiverNo;

    /**
     * 所属通道
     * @see PayChannelEnum
     */
    private String channel;

    /**
     * 分账接收方类型
     * @see AllocReceiverTypeEnum
     */
    private String receiverType;

    /** 接收方账号 */
    private String receiverAccount;

    /** 接收方姓名 */
    private String receiverName;

    /**
     * 分账关系类型
     * @see AllocRelationTypeEnum
     */
    private String relationType;

    /** 关系名称 */
    private String relationName;

}
