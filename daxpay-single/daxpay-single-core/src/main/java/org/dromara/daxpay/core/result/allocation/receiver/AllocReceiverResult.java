package org.dromara.daxpay.core.result.allocation.receiver;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.result.MchAppResult;

import java.util.List;

/**
 * 分账接收方
 * @author xxm
 * @since 2024/3/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账接收方")
public class AllocReceiverResult extends MchAppResult {

    @Schema(description = "接收方列表")
    private List<Receiver> receivers;

    @Data
    @Accessors(chain = true)
    public static class Receiver{
        @Schema(description = "接收方编号")
        private String receiverNo;

        /**
         * @see ChannelEnum
         */
        @Schema(description = "所属通道")
        private String channel;

        /**
         * 分账接收方类型 个人/商户
         * @see org.dromara.daxpay.core.enums.AllocReceiverTypeEnum
         */
        @Schema(description = "分账接收方类型")
        private String receiverType;


        @Schema(description = "接收方账号")
        private String receiverAccount;

        /** 接收方姓名 */
        @Schema(description = "接收方姓名")
        private String receiverName;

        /**
         * 分账关系类型
         * @see org.dromara.daxpay.core.enums.AllocRelationTypeEnum
         */
        @Schema(description = "分账关系类型")
        private String relationType;

        @Schema(description = "关系名称")
        private String relationName;
    }
}
