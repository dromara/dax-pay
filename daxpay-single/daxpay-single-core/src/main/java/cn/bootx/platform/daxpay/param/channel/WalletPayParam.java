package cn.bootx.platform.daxpay.param.channel;

import cn.bootx.platform.daxpay.param.IChannelParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 钱包支付参数
 *
 * @author xxm
 * @since 2020/12/8
 */
@Data
@Schema(title = "钱包支付参数")
public class WalletPayParam implements IChannelParam {

    @Schema(description = "钱包ID")
    private Long walletId;

    @Schema(description = "用户ID")
    private Long userId;

}
