package cn.bootx.platform.daxpay.service.param.channel.wallet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 开通钱包参数
 * @author xxm
 * @since 2024/2/17
 */
@Data
@Accessors(chain = true)
@Schema(title = "开通钱包参数")
public class CreateWalletParam {

    @Schema(description = "用户id")
    @NotBlank(message = "用户ID不可为空")
    private String userId;

    @Schema(description = "钱包名称")
    @NotBlank(message = "钱包名称不可为空")
    private String name;

    @Schema(description = "钱包金额")
    @NotNull(message = "钱包金额不可为空")
    @Min(value = 0, message = "钱包金额不可以低于0")
    private Integer balance;

}
