package cn.bootx.platform.starter.dingtalk.dto.robot;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.starter.data.perm.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "钉钉机器人配置")
public class DingRobotConfigDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = -5642207413191556792L;

    @Schema(description = "编号")
    private String code;

    @Schema(description = "配置名称")
    private String name;

    @Schema(description = "钉钉机器人的AccessToken")
    @SensitiveInfo
    private String accessToken;

    @Schema(description = "是否开启验签")
    private boolean enableSignatureCheck;

    @Schema(description = "验签秘钥")
    @SensitiveInfo
    private String signSecret;

    @Schema(description = "描述")
    private String remark;

}
