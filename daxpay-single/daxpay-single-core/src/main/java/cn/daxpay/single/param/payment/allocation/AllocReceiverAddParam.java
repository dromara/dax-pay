package cn.daxpay.single.param.payment.allocation;

import cn.daxpay.single.code.AllocReceiverTypeEnum;
import cn.daxpay.single.code.AllocRelationTypeEnum;
import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * 分账接收者添加参数
 * @author xxm
 * @since 2024/5/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账接收者添加参数")
public class AllocReceiverAddParam extends PaymentCommonParam {

    @Schema(description = "接收者编号, 需要保证唯一")
    @NotEmpty(message = "接收者编号必填")
    private String receiverNo;

    /**
     * 所属通道
     * @see PayChannelEnum
     */
    @Schema(description = "所属通道")
    @NotEmpty(message = "所属通道必填")
    private String channel;

    /**
     * 分账接收方类型 根据不同类型的通道进行传输
     * @see AllocReceiverTypeEnum
     */
    @Schema(description = "分账接收方类型")
    @NotEmpty(message = "分账接收方类型必填")
    private String receiverType;

    /** 接收方账号 */
    @Schema(description = "接收方账号")
    @NotEmpty(message = "接收方账号必填")
    private String receiverAccount;

    /** 接收方姓名 */
    @Schema(description = "接收方姓名")
    private String receiverName;

    /**
     * 分账关系类型
     * @see AllocRelationTypeEnum
     */
    @Schema(description = "分账关系类型")
    @NotEmpty(message = "分账关系类型必填")
    private String relationType;

    /** 关系名称 关系类型为自定义是填写 */
    @Schema(description = "关系名称")
    private String relationName;
}
