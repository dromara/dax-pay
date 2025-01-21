package cn.bootx.platform.iam.result.user;

import cn.bootx.platform.common.jackson.sensitive.SensitiveInfo;
import cn.bootx.platform.core.entity.UserDetail;
import cn.bootx.platform.iam.code.UserStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @since 2020/4/24
 */
@Data
@Accessors(chain = true)
@Schema(title = "用户信息")
public class UserInfoResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "密码")
    @SensitiveInfo(SensitiveInfo.SensitiveType.PASSWORD)
    private String password;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "是否管理员")
    private boolean administrator;

    /**
     * @see UserStatusEnum
     */
    @Schema(description = "账号状态")
    private String status;


    public UserDetail toUserDetail() {
        return new UserDetail().setId(this.getId())
            .setPassword(this.password)
            .setAccount(this.getAccount())
            .setName(this.name)
            .setAdmin(this.administrator)
            .setStatus(this.status);
    }

}
