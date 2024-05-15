package cn.bootx.platform.starter.wecom.dto.robot;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.starter.data.perm.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 企业微信机器人配置
 *
 * @author bootx
 * @since 2022-07-23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "企业微信机器人配置")
@Accessors(chain = true)
public class WecomRobotConfigDto extends BaseDto {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "编号")
    private String code;

    @SensitiveInfo
    @Schema(description = "webhook地址的key值")
    private String webhookKey;

    @Schema(description = "备注")
    private String remark;

}
