package cn.bootx.platform.iam.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户登录后所需的基础信息信息
 *
 * @author xxm
 * @since 2022/1/8
 */
@Data
@Accessors(chain = true)
@Schema(title = "用户登录后所需的基础信息信息")
public class LoginAfterUserInfo {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "头像")
    private String avatar;

}
