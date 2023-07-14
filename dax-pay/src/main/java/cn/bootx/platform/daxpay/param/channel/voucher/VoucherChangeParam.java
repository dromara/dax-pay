package cn.bootx.platform.daxpay.param.channel.voucher;

import cn.bootx.platform.daxpay.code.paymodel.VoucherCode;
import cn.hutool.core.date.DatePattern;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author xxm
 * @since 2023/7/13
 */
@Data
@Accessors(chain = true)
@Schema(title = "储值卡信息更改参数")
public class VoucherChangeParam {

    @ExcelProperty("卡号")
    @Schema(description = "卡号")
    private String cardNo;

    @ExcelProperty("卡号")
    @Schema(description = "面值")
    private BigDecimal faceValue;

    @ExcelProperty("卡号")
    @Schema(description = "余额")
    private BigDecimal balance;

    @ExcelProperty("卡号")
    @Schema(description = "是否长期有效")
    private Boolean enduring;

    @ExcelProperty("开始时间")
    @Schema(description = "开始时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime startTime;

    @ExcelProperty("结束时间")
    @Schema(description = "结束时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime endTime;

    /**
     * @see VoucherCode#STATUS_NORMAL
     */
    @ExcelProperty("默认状态")
    @Schema(description = "默认状态")
    private String status;

}
