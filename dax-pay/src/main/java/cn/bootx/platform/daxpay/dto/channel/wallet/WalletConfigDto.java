package cn.bootx.platform.daxpay.dto.channel.wallet;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 *
 * @author xxm
 * @since 2023/7/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "钱包配置")
public class WalletConfigDto extends BaseDto {

    @Schema(description = "商户编码")
    private String mchCode;

    @Schema(description = "商户应用编码")
    private String mchAppCode;

    @Schema(description = "默认余额")
    private BigDecimal defaultBalance;

    @Schema(description = "备注")
    private String remark;

}
