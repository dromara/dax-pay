package cn.bootx.platform.daxpay.param.channel.wallet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author xxm
 * @since 2020/12/8
 */
@Data
@Accessors(chain = true)
@Schema(title = "钱包充值参数")
public class WalletRechargeParam implements Serializable {

    private static final long serialVersionUID = 73058709379178254L;

    @NotNull(message = "钱包ID不可为空")
    @Schema(description = "钱包ID")
    private Long walletId;

    @NotNull(message = "充值金额不可为空")
    @Schema(description = "充值金额")
    private Integer amount;
}
