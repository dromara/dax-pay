package cn.bootx.platform.daxpay.service.dto.channel.voucher;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 储值卡配置
 * @author xxm
 * @since 2024/2/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "储值卡配置")
public class VoucherConfigDto extends BaseDto {

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;

    /** 可用支付方式 */
    @Schema(description = "可用支付方式")
    private List<String> payWays;

    /** 单次支持支持多少钱 */
    @Schema(description = "单次支持支持多少钱")
    private Integer singleLimit;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

}
