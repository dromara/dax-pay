package cn.daxpay.single.result.allocation;

import cn.daxpay.single.code.AllocReceiverTypeEnum;
import cn.daxpay.single.code.AllocRelationTypeEnum;
import cn.daxpay.single.code.PayChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 分账接收方
 * @author xxm
 * @since 2024/5/21
 */
@Data
@Accessors(chain = true)
@Schema(title = "分账接收方")
public class AllocReceiverResult {

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
