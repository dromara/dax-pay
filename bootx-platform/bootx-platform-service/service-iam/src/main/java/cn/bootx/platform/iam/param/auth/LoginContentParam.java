package cn.bootx.platform.iam.param.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 前端登录页信息
 * @author xxm
 * @since 2023/8/17
 */
@Data
@Accessors(chain = true)
@Schema(title =  "登录页信息")
public class LoginContentParam {

    @Schema(description = "终端Code")
    private String clientId;

    @Schema(description = "账号")
    private String account;

}
