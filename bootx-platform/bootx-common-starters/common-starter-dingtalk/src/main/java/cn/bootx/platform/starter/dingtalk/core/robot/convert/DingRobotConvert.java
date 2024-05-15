package cn.bootx.platform.starter.dingtalk.core.robot.convert;

import cn.bootx.platform.starter.dingtalk.param.robot.DingRobotConfigParam;
import cn.bootx.platform.starter.dingtalk.core.robot.entity.DingRobotConfig;
import cn.bootx.platform.starter.dingtalk.dto.robot.DingRobotConfigDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 钉钉相关类转换
 *
 * @author xxm
 * @since 2021/8/5
 */
@Mapper
public interface DingRobotConvert {

    DingRobotConvert CONVERT = Mappers.getMapper(DingRobotConvert.class);

    DingRobotConfig convert(DingRobotConfigParam in);

    DingRobotConfigDto convert(DingRobotConfig in);

}
