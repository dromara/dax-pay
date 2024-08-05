package cn.daxpay.multi.service.param.reconcile;

import io.swagger.v3.oas.annotations.media.Schema;
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

    /** 应用Appid */
    @Schema(description = "应用Appid")
    private String appId;

    /** 通道 */
    @Schema(description = "通道")
    private String channel;

    /** 日期 */
    @Schema(description = "日期")
    private LocalDate date;

}
