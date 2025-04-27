package org.dromara.daxpay.channel.alipay.result.allocation;

import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.core.enums.AllocRelationTypeEnum;
import org.dromara.daxpay.service.common.result.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付宝分账接收方绑定
 * @author xxm
 * @since 2025/1/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付宝分账接收方绑定")
public class AlipayAllocReceiverBindResult extends MchResult {

    /** 接收方账号 */
    @Schema(description = "接收方账号")
    private String receiverAccount;

    /**
     * 接收方类型
     * @see AllocReceiverTypeEnum
     */
    @Schema(description = "接收方类型")
    private String receiverType;

    /** 接收方名称 */
    @Schema(description = "接收方名称")
    private String receiverName;

    /**
     * 分账关系类型
     * @see AllocRelationTypeEnum
     */
    @Schema(description = "分账关系类型")
    private String relationType;

    /** 分账关系名称 */
    @Schema(description = "分账关系名称")
    private String relationName;

    /** 是否服务商模式 */
    @Schema(description = "是否服务商模式")
    private boolean isv;

    /** 是否绑定 */
    @Schema(description = "是否绑定")
    private boolean bind;

    /** 错误提示 */
    @Schema(description = "错误提示")
    private String errorMsg;
}
