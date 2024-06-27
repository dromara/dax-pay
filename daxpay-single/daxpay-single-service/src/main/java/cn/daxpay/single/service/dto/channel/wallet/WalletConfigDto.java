package cn.daxpay.single.service.dto.channel.wallet;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

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

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;

    /** 可用支付方式 */
    @Schema(description = "可用支付方式")
    private List<String> payWays;

    /** 单次支持支持多少钱 */
    @Schema(description = "单次支持支持多少钱")
    private Integer limitAmount;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

}
