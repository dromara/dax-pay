package cn.daxpay.open.platform.iam.result.user;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.iam.code.UserStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(title = "用户信息")
public class UserInfoResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "身份域编码")
    private String clientCode;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "密码")
    @SensitiveInfo(SensitiveInfo.SensitiveType.PASSWORD)
    private String password;

    @Schema(description = "邮箱")
    @SensitiveInfo(SensitiveInfo.SensitiveType.EMAIL)
    private String email;

    @Schema(description = "是否管理员")
    private boolean administrator;

    /// @see UserStatusEnum
    @Schema(description = "账号状态")
    private String status;

    public UserDetail toUserDetail() {
        return UserDetail.of(this.getId(), this.name, this.getClientCode(), this.getAccount(),
                this.administrator, this.status);
    }

}

