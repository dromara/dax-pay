package cn.daxpay.open.payment.device.printer.result;

import cn.daxpay.open.payment.device.enums.DeviceStatusEnum;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 云打印设备
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "云打印设备")
public class DevicePrinterResult extends BaseResult {

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "厂商代码")
    private String vendorCode;

    @Schema(description = "厂商配置ID")
    private Long vendorConfigId;

    @Schema(description = "设备序列号(SN)")
    private String deviceSn;

    @Schema(description = "设备IMEI")
    private String imei;

    @Schema(description = "厂商门店ID")
    private String shopId;

    @Schema(description = "设备名称")
    private String deviceName;

    /// 设备状态
    /// @see DeviceStatusEnum
    @Schema(description = "设备状态(unbound未绑定/online在线/offline离线/fault故障)")
    private String status;

    @Schema(description = "绑定时间")
    private OffsetDateTime bindTime;

    @Schema(description = "最后在线时间")
    private OffsetDateTime lastOnlineTime;

    @Schema(description = "备注")
    private String remark;
}
