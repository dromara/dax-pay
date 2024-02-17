package cn.bootx.platform.daxpay.service.param.channel.voucher;

import cn.bootx.platform.daxpay.service.code.VoucherStatusEnum;
import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 储值卡批量导入
 * @author xxm
 * @since 2024/2/17
 */
@Data
@Schema(title = "储值卡导入参数")
public class VoucherBatchImportParam {

    @Schema(description = "卡号")
    public List<String> cardNoList;

    @Schema(description = "面值")
    private Integer faceValue;

    @Schema(description = "余额")
    private Integer balance;

    @Schema(description = "是否长期有效")
    private boolean enduring;

    @Schema(description = "开始时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime endTime;

    /**
     * @see VoucherStatusEnum
     */
    @Schema(description = "默认状态")
    private String status;
}
