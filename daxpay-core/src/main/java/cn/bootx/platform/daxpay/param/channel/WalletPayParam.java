package cn.bootx.platform.daxpay.param.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 钱包支付参数
 *
 * @author xxm
 * @since 2020/12/8
 */
@Data
@Accessors(chain = true)
@Schema(title = "钱包支付参数")
public class WalletPayParam implements Serializable {

    private static final long serialVersionUID = 3255160458016870367L;

    @Schema(description = "钱包ID")
    private Long walletId;

    @Schema(description = "用户ID")
    private Long userId;

}
