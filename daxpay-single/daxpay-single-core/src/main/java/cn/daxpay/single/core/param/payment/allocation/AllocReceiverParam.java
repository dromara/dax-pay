package cn.daxpay.single.core.param.payment.allocation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 分账接收方列表
 * @author xxm
 * @since 2024/5/21
 */
@Data
@Accessors(chain = true)
@Schema(title = "分账接收方列表")
public class AllocReceiverParam {

    /** 分账接收方编号 */
    @Schema(description = "分账接收方编号")
    @NotBlank(message = "分账接收方编号必填")
    @Size(max = 32, message = "分账接收方编号不可超过32位")
    private String receiverNo;

    /** 分账金额 */
    @Schema(description = "分账金额")
    @NotNull(message = "分账金额必填")
    @Min(value = 1,message = "分账金额至少为0.01元")
    private Integer amount;
}
