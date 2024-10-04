package cn.bootx.platform.starter.quartz.dao;

import cn.bootx.platform.starter.quartz.entity.QuartzJob;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务
 *
 * @author xxm
 * @since 2021/11/2
 */
@Mapper
public interface QuartzJobMapper extends MPJBaseMapper<QuartzJob> {

}
