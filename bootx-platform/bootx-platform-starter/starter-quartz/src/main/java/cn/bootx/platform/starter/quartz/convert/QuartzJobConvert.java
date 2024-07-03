package cn.bootx.platform.starter.quartz.convert;

import cn.bootx.platform.starter.quartz.entity.QuartzJob;
import cn.bootx.platform.starter.quartz.entity.QuartzJobLog;
import cn.bootx.platform.starter.quartz.result.QuartzJobResult;
import cn.bootx.platform.starter.quartz.result.QuartzJobLogResult;
import cn.bootx.platform.starter.quartz.param.QuartzJobParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 定时任务转换
 *
 * @author xxm
 * @since 2021/11/2
 */
@Mapper
public interface QuartzJobConvert {

    QuartzJobConvert CONVERT = Mappers.getMapper(QuartzJobConvert.class);

    QuartzJobResult convert(QuartzJob in);

    QuartzJob convert(QuartzJobParam in);

    QuartzJobLogResult convert(QuartzJobLog in);

}
