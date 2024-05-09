package cn.bootx.platform.iam.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户认证状态结果
 * @author xxm
 * @since 2023/9/21
 */
@Data
@Accessors(chain = true)
@Schema(title = "用户认证状态结果")
public class UserVerifyStateResult {

    /** 验证是否通过 */
    private boolean verify;
    /** 认证失败原因 */
    private String reason;

    public static UserVerifyStateResult success(){
        return new UserVerifyStateResult().setVerify(true);
    }

    public static UserVerifyStateResult fail(String reason) {
        return new UserVerifyStateResult().setVerify(false).setReason(reason);
    }

}
