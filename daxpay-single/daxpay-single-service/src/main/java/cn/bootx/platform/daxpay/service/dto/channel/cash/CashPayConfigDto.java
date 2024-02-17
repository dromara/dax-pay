package cn.bootx.platform.daxpay.service.dto.channel.cash;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.table.modify.annotation.DbColumn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 现金支付配置
 * @author xxm
 * @since 2024/2/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "现金支付配置")
public class CashPayConfigDto extends BaseDto {
    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;

    /** 可用支付方式 */
    @DbColumn(comment = "可用支付方式")
    private List<String> payWays;

    /** 单次支持支持多少钱 */
    @Schema(description = "单次支持支持多少钱")
    private Integer singleLimit;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
