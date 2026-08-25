package cn.daxpay.open.platform.iam.param.passkey;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通行密钥登录选项参数
///
@Data
@Accessors(chain = true)
@Schema(title = "通行密钥登录选项参数")
public class PasskeyLoginOptionsParam {

    @Schema(description = "终端编码(admin/merchant)")
    @NotBlank(message = "{validation.field.client.notBlank}")
    private String client;
}
