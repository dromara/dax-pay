package cn.bootx.platform.starter.wecom.core.robot.convert;

import cn.bootx.platform.starter.wecom.param.robot.WecomRobotConfigParam;
import cn.bootx.platform.starter.wecom.core.robot.entity.WecomRobotConfig;
import cn.bootx.platform.starter.wecom.dto.robot.WecomRobotConfigDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 企业微信机器人配置
 *
 * @author bootx
 * @since 2022-07-23
 */
@Mapper
public interface WecomRobotConfigConvert {

    WecomRobotConfigConvert CONVERT = Mappers.getMapper(WecomRobotConfigConvert.class);

    WecomRobotConfig convert(WecomRobotConfigParam in);

    WecomRobotConfigDto convert(WecomRobotConfig in);

}
