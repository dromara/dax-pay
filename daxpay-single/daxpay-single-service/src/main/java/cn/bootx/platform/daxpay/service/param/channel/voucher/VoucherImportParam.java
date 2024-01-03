package cn.bootx.platform.daxpay.service.param.channel.voucher;

import cn.bootx.platform.daxpay.service.code.VoucherCode;
import cn.hutool.core.date.DatePattern;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author xxm
 * @since 2022/3/14
 */
@Data
@Schema(title = "储值卡导入参数")
public class VoucherImportParam {

    @ExcelProperty("卡号")
    @Schema(description = "卡号")
    private String cardNo;

    @ExcelProperty("面值")
    @Schema(description = "面值")
    private BigDecimal faceValue;

    @ExcelProperty("余额")
    @Schema(description = "余额")
    private BigDecimal balance;

    @ExcelProperty("是否长期有效")
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
