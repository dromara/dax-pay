package cn.daxpay.open.payment.device.vendor.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 设备厂商配置查询参数
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Accessors(chain = true)
@Schema(title = "设备厂商配置查询参数")
public class DeviceVendorConfigQuery {

    /// 设备类型
    @Schema(description = "设备类型")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String deviceType;

    /// 厂商代码
    @Schema(description = "厂商代码")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String vendorCode;

    /// 配置名称
    @Schema(description = "配置名称")
    private String configName;

    /// 是否启用
    @Schema(description = "是否启用")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private Boolean enable;
}
