package cn.bootx.platform.iam.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author xxm
 * @since 2021/6/2
 */
@Data
@Accessors(chain = true)
@Schema(title = "用户信息参数")
public class UserInfoParam implements Serializable {

    private static final long serialVersionUID = -1263052439212534900L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "登录账号")
    private String username;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "终端id列表")
    private List<Long> clientIds;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "密码")
    private String password;

}
