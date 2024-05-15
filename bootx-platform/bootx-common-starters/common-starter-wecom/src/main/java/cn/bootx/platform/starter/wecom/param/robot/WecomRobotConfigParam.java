package cn.bootx.platform.starter.wecom.param.robot;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 企业微信机器人配置
 *
 * @author bootx
 * @since 2022-07-23
 */
@Data
@Schema(title = "企业微信机器人配置")
@Accessors(chain = true)
public class WecomRobotConfigParam {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "编号")
    private String code;

    @Schema(description = "webhook地址的key值")
    private String webhookKey;

    @Schema(description = "备注")
    private String remark;

}
