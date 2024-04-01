package cn.bootx.platform.daxpay.service.param.allocation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

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
public class AllocationGroupReceiverParam {

    @NotNull(message = "接收者ID不可为空")
    @Schema(description = "接收者ID")
    private Long receiverId;

    @Schema(description = "分账比例(万分之多少)")
    @NotNull(message = "分账比例不可为空")
    @Min(value = 1,message = "分账比例最低为1")
    private Integer rate;
}
