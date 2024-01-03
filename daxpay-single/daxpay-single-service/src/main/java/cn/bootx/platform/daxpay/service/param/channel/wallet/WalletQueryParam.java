package cn.bootx.platform.daxpay.service.param.channel.wallet;

import cn.bootx.platform.common.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @since 2020/12/8
 */
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Accessors(chain = true)
@Schema(title = "钱包查询参数")
public class WalletQueryParam {

    @Schema(description = "钱包ID")
    private Long walletId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "商户编码")
    private String mchCode;

    @Schema(description = "商户应用编码")
    private String mchAppCode;
}
