package cn.daxpay.single.service.param.channel.wallet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 钱包充值参数
 * @author xxm
 * @since 2020/12/8
 */
@Data
@Accessors(chain = true)
@Schema(title = "钱包充值参数")
public class WalletRechargeParam {

    @Schema(description = "钱包ID")
    private Long walletId;

    @Schema(description = "用户ID")
    private String userId;

    @NotNull(message = "充值金额不可为空")
    @Min(value = 1,message = "充值金额最少为1分")
    @Schema(description = "充值金额")
    private Integer amount;
}
