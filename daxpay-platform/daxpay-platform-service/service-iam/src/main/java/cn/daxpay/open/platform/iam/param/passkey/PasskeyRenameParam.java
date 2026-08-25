package cn.daxpay.open.platform.iam.param.passkey;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通行密钥改名参数
///
@Data
@Accessors(chain = true)
@Schema(title = "通行密钥改名参数")
public class PasskeyRenameParam {

    @Schema(description = "凭据记录ID")
    @NotNull(message = "{validation.field.id.notNull}")
    private Long id;

    @Schema(description = "设备可辨识名")
    @NotBlank(message = "{validation.field.deviceName.notBlank}")
    private String deviceName;
}
