package org.dromara.daxpay.service.param.allocation.group;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 分账组绑定参数
 * @author xxm
 * @since 2024/4/1
 */
@Data
@Accessors(chain = true)
@Schema(title = "分账组绑定参数")
public class AllocGroupBindParam {

    @NotNull(message = "分账组ID不可为空")
    @Schema(description = "分账组ID")
    private Long groupId;

    @Valid
    @NotEmpty(message = "分账接收方不可为空")
    @Schema(description = "分账接收方集合")
    private List<AllocGroupReceiverParam> receivers;
}
