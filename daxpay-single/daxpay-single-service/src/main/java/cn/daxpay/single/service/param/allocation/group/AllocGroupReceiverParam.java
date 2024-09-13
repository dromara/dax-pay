package cn.daxpay.single.service.param.allocation.group;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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

    @Schema(description = "分账比例(万分之多少)")
    @NotNull(message = "分账比例不可为空")
    @Min(value = 0,message = "分账比例不可为负数")
    @Max(value = 10000,message = "分账比例不可超过100%")
    private Integer rate;
}
