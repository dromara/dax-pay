package cn.bootx.platform.starter.quartz.core.dao;

import cn.bootx.platform.starter.quartz.core.entity.QuartzJobLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务日志
 *
 * @author xxm
 * @since 2022/5/1
 */
@Mapper
public interface QuartzJobLogMapper extends BaseMapper<QuartzJobLog> {

}
