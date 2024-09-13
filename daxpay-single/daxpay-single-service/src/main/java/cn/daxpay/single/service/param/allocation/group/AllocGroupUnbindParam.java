package cn.daxpay.single.service.param.allocation.group;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 分账组接收者解除绑定
 * @author xxm
 * @since 2024/4/1
 */
@Data
@Accessors(chain = true)
@Schema(title = "分账组取消接收者绑定")
public class AllocGroupUnbindParam {

    @NotNull(message = "分账组ID不可为空")
    @Schema(description = "分账组ID")
    private Long groupId;

    @NotBlank(message = "分账接收方不可为空")
    @Schema(description = "分账接收方集合")
    List<Long> receiverIds;
}
