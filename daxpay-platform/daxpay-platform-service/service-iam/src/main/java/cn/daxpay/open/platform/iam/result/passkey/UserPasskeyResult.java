package cn.daxpay.open.platform.iam.result.passkey;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 用户通行密钥结果
///
@Data
@Accessors(chain = true)
@Schema(title = "用户通行密钥结果")
public class UserPasskeyResult {

    @Schema(description = "凭据记录ID")
    private Long id;

    @Schema(description = "设备可辨识名")
    private String deviceName;

    @Schema(description = "凭据传输方式(internal/hybrid/usb/nfc/ble, 逗号分隔)")
    private String transports;

    @Schema(description = "是否多设备同步凭据")
    private Boolean backupEligible;

    @Schema(description = "是否处于同步状态")
    private Boolean backupState;

    @Schema(description = "创建时间")
    private OffsetDateTime createTime;

    @Schema(description = "最后使用时间")
    private OffsetDateTime lastUsedTime;
}
