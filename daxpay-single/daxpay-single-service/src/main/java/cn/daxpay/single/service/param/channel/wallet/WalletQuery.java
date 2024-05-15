package cn.daxpay.single.service.param.channel.wallet;

import cn.bootx.platform.common.core.annotation.QueryParam;
import cn.bootx.platform.common.core.rest.param.QueryOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @since 2020/12/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Accessors(chain = true)
@Schema(title = "钱包查询参数")
public class WalletQuery extends QueryOrder {

    @Schema(description = "钱包ID")
    private Long walletId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "商户编码")
    private String mchCode;

    @Schema(description = "商户应用编码")
    private String mchAppCode;
}
