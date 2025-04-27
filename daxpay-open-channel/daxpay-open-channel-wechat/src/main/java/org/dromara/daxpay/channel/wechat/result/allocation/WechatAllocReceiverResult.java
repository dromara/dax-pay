package org.dromara.daxpay.channel.wechat.result.allocation;

import org.dromara.daxpay.service.common.result.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 微信分账接收方
 * @author xxm
 * @since 2025/1/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "微信分账接收方")
public class WechatAllocReceiverResult extends MchResult {

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

    /** 分账关系类型 */
    @Schema(description = "分账关系类型")
    private String relationType;

    /** 分账关系名称 */
    @Schema(description = "分账关系名称")
    private String relationName;
}
