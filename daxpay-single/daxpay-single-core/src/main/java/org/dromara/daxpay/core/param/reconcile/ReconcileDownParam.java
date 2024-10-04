package org.dromara.daxpay.core.param.reconcile;

import org.dromara.daxpay.core.param.PaymentCommonParam;
import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 对账文件下载参数
 * @author xxm
 * @since 2024/8/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "对账下载参数")
public class ReconcileDownParam extends PaymentCommonParam {
    @Schema(description = "通道")
    @Size(max = 32, message = "通道不可超过32位")
    @NotBlank(message = "通道不可为空")
    private String channel;

    @Schema(description = "日期")
    @NotNull(message = "日期不可为空")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate date;
}
