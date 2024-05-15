package cn.bootx.platform.starter.quartz.core.convert;

import cn.bootx.platform.starter.quartz.param.QuartzJobParam;
import cn.bootx.platform.starter.quartz.core.entity.QuartzJob;
import cn.bootx.platform.starter.quartz.core.entity.QuartzJobLog;
import cn.bootx.platform.starter.quartz.dto.QuartzJobDto;
import cn.bootx.platform.starter.quartz.dto.QuartzJobLogDto;
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

    QuartzJobDto convert(QuartzJob in);

    QuartzJob convert(QuartzJobParam in);

    QuartzJobLogDto convert(QuartzJobLog in);

}
