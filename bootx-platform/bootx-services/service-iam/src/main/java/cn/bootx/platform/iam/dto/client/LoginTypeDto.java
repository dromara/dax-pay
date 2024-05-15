package cn.bootx.platform.iam.dto.client;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 登录方式
 *
 * @author xxm
 * @since 2021/8/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "登录方式")
public class LoginTypeDto extends BaseDto {

    @Schema(description = "编码")
    private String code;

    @Schema(description = "名称")
    private String name;

    /**
     * password 密码登录, openId 第三方登录
     */
    @Schema(description = "类型")
    private String type;

    @Schema(description = "是否系统内置")
    private boolean internal;

    @Schema(description = "在线时长 分钟")
    private Long timeout;

    @Schema(description = "是否启用验证码")
    private boolean captcha;

    @Schema(description = "密码错误几次冻结 -1表示不限制")
    private Integer pwdErrNum;

    @Schema(description = "是否可用")
    private boolean enable;

    @Schema(description = "描述")
    private String description;

}
