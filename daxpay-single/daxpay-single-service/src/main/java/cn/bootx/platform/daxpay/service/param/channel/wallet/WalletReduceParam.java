package cn.bootx.platform.daxpay.service.param.channel.wallet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 钱包扣减参数
 * @author xxm
 * @since 2024/2/17
 */
@Data
@Accessors(chain = true)
@Schema(title = "钱包扣减参数")
public class WalletReduceParam {


    @Schema(description = "钱包ID")
    private Long walletId;

    @Schema(description = "用户ID")
    private String userId;

    @NotNull(message = "扣减金额不可为空")
    @Min(value = 1,message = "扣减金额最少为1分")
    @Schema(description = "扣减金额")
    private Integer amount;
}
