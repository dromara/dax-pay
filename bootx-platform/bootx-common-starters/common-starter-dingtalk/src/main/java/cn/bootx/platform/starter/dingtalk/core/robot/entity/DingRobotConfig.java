package cn.bootx.platform.starter.dingtalk.core.robot.entity;

import cn.bootx.platform.starter.dingtalk.core.robot.convert.DingRobotConvert;
import cn.bootx.platform.starter.dingtalk.param.robot.DingRobotConfigParam;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.starter.dingtalk.dto.robot.DingRobotConfigDto;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 钉钉机器人配置
 *
 * @author xxm
 * @since 2020/11/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("starter_ding_robot_config")
public class DingRobotConfig extends MpBaseEntity implements EntityBaseFunction<DingRobotConfigDto> {

    /** 机器人编号编号 */
    private String code;

    /** 机器人配置名称 */
    private String name;

    /** 钉钉机器人的accessToken */
    private String accessToken;

    /** 是否开启验签 */
    private boolean enableSignatureCheck;

    /** 验签秘钥 */
    private String signSecret;

    /** 备注 */
    private String remark;

    public static DingRobotConfig init(DingRobotConfigParam in) {
        return DingRobotConvert.CONVERT.convert(in);
    }

    @Override
    public DingRobotConfigDto toDto() {
        return DingRobotConvert.CONVERT.convert(this);
    }

}
