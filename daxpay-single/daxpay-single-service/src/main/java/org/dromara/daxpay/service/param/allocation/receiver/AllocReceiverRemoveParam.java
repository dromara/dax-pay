package org.dromara.daxpay.service.param.allocation.receiver;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;

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

    @Schema(description = "商户应用ID")
    @NotBlank(message = "商户应用ID必填")
    @Size(max = 32, message = "商户应用ID不可超过32位")
    private String appId;
}
