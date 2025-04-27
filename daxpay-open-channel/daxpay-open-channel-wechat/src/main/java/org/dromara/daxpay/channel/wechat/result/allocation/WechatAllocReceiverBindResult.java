package org.dromara.daxpay.channel.wechat.result.allocation;

import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.service.common.result.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 微信分账接收方绑定
 * @author xxm
 * @since 2025/1/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "微信分账接收方绑定")
public class WechatAllocReceiverBindResult extends MchResult {

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

    /** 分账关系类型 */
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
