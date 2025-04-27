package org.dromara.daxpay.channel.wechat.param.allocation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信分账接收方
 * @author xxm
 * @since 2025/1/26
 */
@Data
@Accessors(chain = true)
@Schema(title = "微信分账接收方")
public class WechatAllocReceiverParam {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 接收方名称 */
    @Schema(description = "接收方名称")
    private String receiverName;

    /** 接收方账号 */
    @NotNull(message = "接收方账号不可为空")
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

    /** 是否为服务商 */
    @Schema(description = "是否为服务商")
    private boolean isv;

    /** 商户AppId */
    @NotNull(message = "商户AppId不可为空")
    @Schema(description = "商户AppId")
    private String appId;
}
