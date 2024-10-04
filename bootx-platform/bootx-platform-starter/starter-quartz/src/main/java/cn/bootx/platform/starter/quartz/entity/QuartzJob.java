package cn.bootx.platform.starter.quartz.entity;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.starter.quartz.code.QuartzJobCode;
import cn.bootx.platform.starter.quartz.convert.QuartzJobConvert;
import cn.bootx.platform.starter.quartz.result.QuartzJobResult;
import cn.bootx.platform.starter.quartz.param.QuartzJobParam;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 定时任务
 *
 * @author xxm
 * @since 2021/11/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("starter_quartz_job")
public class QuartzJob extends MpBaseEntity implements ToResult<QuartzJobResult> {

    /** 任务名称 */
    private String name;

    /** 任务类名 */
    private String jobClassName;

    /** 分组 */
    private String groupName;

    /** cron表达式 */
    private String cron;

    /** 参数 */
    private String parameter;

    /**
     * 状态
     * @see QuartzJobCode
     */
    private Integer state;

    /** 备注 */
    private String remark;

    @Override
    public QuartzJobResult toResult() {
        return QuartzJobConvert.CONVERT.convert(this);
    }

    public static QuartzJob init(QuartzJobParam in) {
        return QuartzJobConvert.CONVERT.convert(in);
    }

}
