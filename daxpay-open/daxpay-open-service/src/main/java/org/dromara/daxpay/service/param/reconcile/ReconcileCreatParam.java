package org.dromara.daxpay.service.param.reconcile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * 对账任务创建参数
 * @author xxm
 * @since 2024/8/5
 */
@Data
@Accessors(chain = true)
@Schema(title = "对账任务创建参数")
public class ReconcileCreatParam {

    /** 名称 */
    @Schema(description = "名称")
    @NotBlank(message = "名称不可为空")
    private String title;

    /** 通道 */
    @Schema(description = "通道")
    @NotBlank(message = "通道不可为空")
    private String channel;

    /** 日期 */
    @Schema(description = "日期")
    @NotNull(message = "日期不可为空")
    private LocalDate date;

    /** 应用Appid */
    @Schema(description = "应用Appid")
    @NotBlank(message = "应用Appid不可为空")
    private String appId;

}
