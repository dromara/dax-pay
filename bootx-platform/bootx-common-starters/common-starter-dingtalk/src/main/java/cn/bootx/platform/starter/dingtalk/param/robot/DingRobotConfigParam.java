package cn.bootx.platform.starter.dingtalk.param.robot;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xxm
 * @since 2021/8/5
 */
@Data
@Accessors(chain = true)
@Schema(title = "钉钉机器人配置参数")
public class DingRobotConfigParam implements Serializable {

    private static final long serialVersionUID = -3979174622716815670L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "编号")
    private String code;

    @Schema(description = "配置名称")
    private String name;

    @Schema(description = "钉钉机器人的accessToken")
    private String accessToken;

    @Schema(description = "是否开启验签")
    private boolean enableSignatureCheck;

    @Schema(description = "验签秘钥")
    private String signSecret;

    @Schema(description = "描述")
    private String remark;

}
