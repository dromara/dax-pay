package cn.bootx.daxpay.param.paymodel.voucher;

import cn.bootx.daxpay.code.paymodel.VoucherCode;
import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author xxm
 * @date 2022/3/14
 */
@Data
@Accessors(chain = true)
@Schema(title = "储值卡生成参数")
public class VoucherGenerationParam {

    @Schema(description = "数量")
    private Integer count;

    @Schema(description = "面值")
    private BigDecimal faceValue;

    @Schema(description = "是否长期有效")
    private Boolean enduring;

    @Schema(description = "开始时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime endTime;

    /**
     * @see VoucherCode
     */
    @Schema(description = "默认状态")
    private Integer status;

}
