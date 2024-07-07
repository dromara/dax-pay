package cn.bootx.platform.iam.result.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 登录页上下⽂信息
 * @author xxm
 * @since 2023/8/17
 */
@Data
@Accessors(chain = true)
@Schema(title =  "登录页上下⽂信息")
public class LoginContentResult {

    /** 支持登录方式 */
    @Schema(description = "支持登录方式")
    private List<String> loginTypes;

    @Schema(description = "是否启用验证码")
    private boolean enableCaptcha;

    @Schema(description = "密码是否加密传输")
    private boolean passwordEncrypted;

}
