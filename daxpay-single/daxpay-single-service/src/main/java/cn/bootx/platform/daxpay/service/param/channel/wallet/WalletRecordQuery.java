package cn.bootx.platform.daxpay.service.param.channel.wallet;

import cn.bootx.platform.common.core.annotation.QueryParam;
import cn.bootx.platform.common.core.rest.param.QueryOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 钱包记录查询参数
 * @author xxm
 * @since 2024/2/18
 */
@EqualsAndHashCode(callSuper = true)
@QueryParam
@Data
@Accessors(chain = true)
@Schema(title = "钱包记录查询参数")
public class WalletRecordQuery extends QueryOrder {

    @Schema(description = "钱包id")
    private Long walletId;
}
