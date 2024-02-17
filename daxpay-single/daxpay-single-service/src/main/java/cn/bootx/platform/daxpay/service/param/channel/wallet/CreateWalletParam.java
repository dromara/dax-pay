package cn.bootx.platform.daxpay.service.param.channel.wallet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

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
    private String userId;

    @Schema(description = "钱包名称")
    private String name;

    @Schema(description = "钱包金额")
    private Integer balance;

}
