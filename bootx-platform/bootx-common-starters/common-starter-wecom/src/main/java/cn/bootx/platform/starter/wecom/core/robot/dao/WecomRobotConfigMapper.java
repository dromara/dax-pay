package cn.bootx.platform.starter.wecom.core.robot.dao;

import cn.bootx.platform.starter.wecom.core.robot.entity.WecomRobotConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 企业微信机器人配置
 *
 * @author bootx
 * @since 2022-07-23
 */
@Mapper
public interface WecomRobotConfigMapper extends BaseMapper<WecomRobotConfig> {

}
