package cn.bootx.platform.starter.quartz.core.dao;

import cn.bootx.platform.starter.quartz.core.entity.QuartzJob;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务
 *
 * @author xxm
 * @since 2021/11/2
 */
@Mapper
public interface QuartzJobMapper extends BaseMapper<QuartzJob> {

}
