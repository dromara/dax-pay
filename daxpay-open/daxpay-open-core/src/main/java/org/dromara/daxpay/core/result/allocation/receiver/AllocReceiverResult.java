package org.dromara.daxpay.core.result.allocation.receiver;

import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 分账接收方
 * @author xxm
 * @since 2024/3/28
 */
@Data
@Accessors(chain = true)
@Schema(title = "分账接收方")
public class AllocReceiverResult {

    @Schema(description = "接收方列表")
    private List<Receiver> receivers;

    @Data
    @Accessors(chain = true)
    public static class Receiver{
        @Schema(description = "接收方编号")
        private String receiverNo;

        /** 接收方名称 */
        @Schema(description = "接收方名称")
        private String receiverName;

        /**
         * 接收方类型
         * @see AllocReceiverTypeEnum
         */
        @Schema(description = "接收方类型")
        private String receiverType;

        @Schema(description = "接收方账号")
        private String receiverAccount;

        /**
         * 所属通道
         * @see ChannelEnum
         */
        @Schema(description = "所属通道")
        private String channel;

    }
}
