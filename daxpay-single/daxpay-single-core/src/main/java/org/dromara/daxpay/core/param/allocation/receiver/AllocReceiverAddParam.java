package org.dromara.daxpay.core.param.allocation.receiver;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.core.enums.AllocRelationTypeEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.param.PaymentCommonParam;


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
    @NotBlank(message = "接收者编号必填")
    @Size(max = 32, message = "接收者编号不可超过32位")
    private String receiverNo;

    /**
     * 所属通道
     * @see ChannelEnum
     */
    @Schema(description = "所属通道")
    @NotBlank(message = "所属通道必填")
    @Size(max = 20, message = "所属通道不可超过20位")
    private String channel;

    /**
     * 分账接收方类型 根据不同类型的通道进行传输
     * @see AllocReceiverTypeEnum
     */
    @Schema(description = "分账接收方类型")
    @NotBlank(message = "分账接收方类型必填")
    @Size(max = 20, message = "分账接收方类型不可超过20位")
    private String receiverType;

    /** 接收方账号 */
    @Schema(description = "接收方账号")
    @NotBlank(message = "接收方账号必填")
    @Size(max = 100, message = "接收方账号不可超过100位")
    private String receiverAccount;

    /** 接收方姓名 */
    @Schema(description = "接收方姓名")
    @Size(max = 100, message = "接收方姓名不可超过50位")
    private String receiverName;

    /**
     * 分账关系类型
     * @see AllocRelationTypeEnum
     */
    @Schema(description = "分账关系类型")
    @NotBlank(message = "分账关系类型必填")
    @Size(max = 20, message = "分账关系类型不可超过20位")
    private String relationType;

    /** 关系名称 关系类型为自定义是填写 */
    @Schema(description = "关系名称")
    @Size(max = 50, message = "关系名称不可超过50位")
    private String relationName;

}
