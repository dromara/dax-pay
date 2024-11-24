package org.dromara.daxpay.service.param.allocation.group;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 分账组接收者参数
 * @author xxm
 * @since 2024/4/1
 */
@Data
@Accessors(chain = true)
@Schema(title = "分账组接收者参数")
public class AllocGroupReceiverParam {

    @NotNull(message = "接收者ID不可为空")
    @Schema(description = "接收者ID")
    private Long receiverId;

    @Schema(description = "分账比例(百分之多少)")
    @NotNull(message = "分账比例不可为空")
    @DecimalMax(value = "100",message = "分账比例不可大于100%")
    @DecimalMin(value = "0.00", message = "分账比例不可为负数")
    @Digits(integer = 3, fraction = 2, message = "分账比例最多只允许两位小数")
    private BigDecimal rate;
}
