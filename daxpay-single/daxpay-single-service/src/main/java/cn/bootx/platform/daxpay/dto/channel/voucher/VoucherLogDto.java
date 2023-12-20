package cn.bootx.platform.daxpay.dto.channel.voucher;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.code.VoucherCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 储值卡日志
 *
 * @author xxm
 * @since 2022/3/17
 */
@Schema(title = "储值卡日志")
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class VoucherLogDto extends BaseDto {

    /** 储值卡id */
    @Schema(description = "储值卡id")
    private Long voucherId;

    /** 储值卡号 */
    @Schema(description = "储值卡号")
    private String voucherNo;

    @Schema(description = "商户编码")
    private String mchCode;

    @Schema(description = "商户应用编码")
    private String mchAppCode;

    /** 金额 */
    @Schema(description = "金额")
    private BigDecimal amount;

    /**
     * 类型
     * @see VoucherCode#LOG_PAY
     */
    @Schema(description = "类型")
    private String type;

    /** 交易记录ID */
    @Schema(description = "交易记录ID")
    private Long paymentId;

    /** 业务ID */
    @Schema(description = "业务ID")
    private String businessId;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

}
