package org.dromara.daxpay.core.param.allocation.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 分账接收方列表
 * @author xxm
 * @since 2024/5/21
 */
@Data
@Accessors(chain = true)
@Schema(title = "分账接收方列表")
public class ReceiverParam {

    /** 分账接收方编号 */
    @Schema(description = "分账接收方编号")
    @NotBlank(message = "分账接收方编号必填")
    @Size(max = 32, message = "分账接收方编号不可超过32位")
    private String receiverNo;

    /** 分账金额 */
    @Schema(description = "分账金额")
    @NotNull(message = "分账金额必填")
    @Min(value = 1,message = "分账金额至少为0.01元")
    private BigDecimal amount;
}
