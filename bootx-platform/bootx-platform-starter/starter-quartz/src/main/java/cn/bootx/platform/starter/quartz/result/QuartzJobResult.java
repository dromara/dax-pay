package cn.bootx.platform.starter.quartz.result;

import cn.bootx.platform.starter.quartz.code.QuartzJobCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 定时任务
 *
 * @author xxm
 * @since 2021/11/2
 */
@Data
@Accessors(chain = true)
@Schema(title = "定时任务")
public class QuartzJobResult{

    @Schema(description = "主键")
    private Long id;

    /** 任务名称 */
    @Schema(description = "任务名称")
    private String name;

    /** 任务类名 */
    @Schema(description = "任务类名")
    private String jobClassName;

    /** cron表达式 */
    @Schema(description = "cron表达式")
    private String cron;

    /** 参数 */
    @Schema(description = "参数")
    private String parameter;

    /**
     * 状态
     * @see QuartzJobCode
     */
    @Schema(description = "状态")
    private Integer state;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

}
