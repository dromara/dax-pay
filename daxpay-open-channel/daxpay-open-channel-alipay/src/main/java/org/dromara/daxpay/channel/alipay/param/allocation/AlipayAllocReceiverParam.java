package org.dromara.daxpay.channel.alipay.param.allocation;

import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.core.enums.AllocRelationTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付宝分账接收方
 * @author xxm
 * @since 2025/1/26
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付宝分账接收方")
public class AlipayAllocReceiverParam {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 接收方名称 */
    @NotBlank(message = "接收方名称不能为空")
    @Schema(description = "接收方名称")
    private String receiverName;

    /** 接收方账号 */
    @NotBlank(message = "接收方账号不能为空")
    @Schema(description = "接收方账号")
    private String receiverAccount;

    /**
     * 接收方类型
     * @see AllocReceiverTypeEnum
     */
    @NotBlank(message = "接收方类型不能为空")
    @Schema(description = "接收方类型")
    private String receiverType;

    /**
     * 分账关系类型
     * @see AllocRelationTypeEnum
     */
    @Schema(description = "分账关系类型")
    private String relationType;

    /** 分账关系名称 */
    @Schema(description = "分账关系名称")
    private String relationName;

    /** 是否为服务商 */
    @Schema(description = "是否为服务商")
    private boolean isv;

    /** 商户AppId */
    @NotBlank(message = "商户AppId不能为空")
    @Schema(description = "商户AppId")
    private String appId;
}
