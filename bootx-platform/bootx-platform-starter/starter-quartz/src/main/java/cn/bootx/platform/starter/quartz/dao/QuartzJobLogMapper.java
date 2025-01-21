package cn.bootx.platform.starter.quartz.dao;

import cn.bootx.platform.starter.quartz.entity.QuartzJobLog;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务日志
 *
 * @author xxm
 * @since 2022/5/1
 */
@Mapper
public interface QuartzJobLogMapper extends MPJBaseMapper<QuartzJobLog> {

}
