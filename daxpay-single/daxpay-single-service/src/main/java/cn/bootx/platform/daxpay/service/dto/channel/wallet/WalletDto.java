package cn.bootx.platform.daxpay.service.dto.channel.wallet;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author xxm
 * @since 2020/12/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "钱包")
public class WalletDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = -1563719305334334625L;

    @Schema(description = "ID,钱包的唯一标识")
    private Long id;

    @Schema(description = "钱包关联的账号ID")
    private Long userId;

    @Schema(description = "商户编码")
    private String mchCode;

    @Schema(description = "商户应用编码")
    private String mchAppCode;

    @Schema(description = "钱包余额")
    private BigDecimal balance;

    @Schema(description = "预冻结额度")
    private BigDecimal freezeBalance;

    @Schema(description = "状态")
    private String status;

}
