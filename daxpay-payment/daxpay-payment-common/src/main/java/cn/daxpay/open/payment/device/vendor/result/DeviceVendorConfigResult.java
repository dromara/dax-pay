package cn.daxpay.open.payment.device.vendor.result;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 设备厂商配置
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "设备厂商配置")
public class DeviceVendorConfigResult extends BaseResult {

    @Schema(description = "设备类型")
    private String deviceType;

    @Schema(description = "厂商代码")
    private String vendorCode;

    @Schema(description = "配置名称")
    private String configName;

    @Schema(description = "厂商应用ID")
    private String appId;

    /// 厂商应用密钥(脱敏返回, 保留前后各4位)
    @SensitiveInfo(front = 4, end = 4)
    @Schema(description = "厂商应用密钥(脱敏)")
    private String appSecret;

    @Schema(description = "是否启用")
    private boolean enable;

    @Schema(description = "扩展参数(JSON)")
    private String extParam;

    @Schema(description = "备注")
    private String remark;
}
