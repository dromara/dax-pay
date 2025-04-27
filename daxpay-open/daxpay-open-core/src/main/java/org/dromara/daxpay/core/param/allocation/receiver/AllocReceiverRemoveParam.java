package org.dromara.daxpay.core.param.allocation.receiver;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 分账接收者删除参数
 * @author xxm
 * @since 2024/5/20
 */
@Data
@Accessors(chain = true)
@Schema(title = "分账接收者删除参数")
public class AllocReceiverRemoveParam {

    @Schema(description = "接收者编号")
    @NotBlank(message = "接收者编号必填")
    @Size(max = 32, message = "接收者编号不可超过32位")
    private String receiverNo;

}
