package org.dromara.daxpay.channel.alipay.result.allocation;

import org.dromara.daxpay.core.enums.AllocRelationTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class AlipayAllocReceiverResult {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 分账接收方编号 */
    @Schema(description = "分账接收方编号")
    private String receiverNo;

    /** 接收方名称 */
    @Schema(description = "接收方名称")
    private String receiverName;

    /** 接收方账号 */
    @Schema(description = "接收方账号")
    private String receiverAccount;

    /** 接收方类型 */
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

    /** 商户AppId */
    @Schema(description = "商户AppId")
    private String appId;
}
