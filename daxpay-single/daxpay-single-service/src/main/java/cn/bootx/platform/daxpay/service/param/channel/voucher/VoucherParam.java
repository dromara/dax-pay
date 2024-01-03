package cn.bootx.platform.daxpay.service.param.channel.voucher;

import cn.bootx.platform.daxpay.service.code.VoucherCode;
import cn.hutool.core.date.DatePattern;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author xxm
 * @since 2022/3/14
 */
@Data
@Accessors(chain = true)
@Schema(title = "储值卡查询参数")
public class VoucherParam {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "卡号")
    private String cardNo;

    @Schema(description = "生成批次号")
    private Long batchNo;

    @Schema(description = "面值")
    private BigDecimal faceValue;

    @Schema(description = "余额")
    private BigDecimal balance;

    @Schema(description = "是否长期有效")
    private Boolean enduring;

    @Schema(description = "开始时间")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime endTime;

    /**
     * @see VoucherCode
     */
    @Schema(description = "状态")
    private Integer status;

}
