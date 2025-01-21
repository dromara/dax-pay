package org.dromara.daxpay.core.param.allocation.receiver;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.param.PaymentCommonParam;

/**
 * 分账接收方查询参数
 * @author xxm
 * @since 2024/10/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账接收方")
public class AllocReceiverQueryParam extends PaymentCommonParam {
    /**
     * 所属通道
     * @see ChannelEnum
     */
    @Schema(description = "所属通道")
    @NotBlank(message = "所属通道必填")
    @Size(max = 20, message = "所属通道不可超过20位")
    private String channel;

}
