package org.dromara.daxpay.service.result.allocation.receiver;

import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.common.result.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分账接收方
 * @author xxm
 * @since 2024/3/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账接收方")
public class AllocReceiverVo extends MchResult {

    @Schema(description = "接收方编号")
    private String receiverNo;

    /**
     * 接收方类型
     * @see AllocReceiverTypeEnum
     */
    @Schema(description = "接收方类型")
    private String receiverType;

    @Schema(description = "接收方账号")
    private String receiverAccount;

    @Schema(description = "接收方名称")
    private String receiverName;

    /**
     * @see ChannelEnum
     */
    @Schema(description = "所属通道")
    private String channel;

}
