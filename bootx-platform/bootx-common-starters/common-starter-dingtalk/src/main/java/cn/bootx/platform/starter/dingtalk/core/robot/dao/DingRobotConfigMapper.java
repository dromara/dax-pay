package cn.bootx.platform.starter.dingtalk.core.robot.dao;

import cn.bootx.platform.starter.dingtalk.core.robot.entity.DingRobotConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 钉钉机器人配置
 *
 * @author xxm
 * @since 2021/8/5
 */
@Mapper
public interface DingRobotConfigMapper extends BaseMapper<DingRobotConfig> {

}
