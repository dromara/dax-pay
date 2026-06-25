package cn.daxpay.open.payment.device.speaker.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 云音箱设备查询参数
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Accessors(chain = true)
@Schema(title = "云音箱设备查询参数")
public class DeviceSpeakerQuery {

    /// 商户号
    @Schema(description = "商户号")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String mchNo;

    /// 厂商代码
    @Schema(description = "厂商代码")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String vendorCode;

    /// 设备序列号
    @Schema(description = "设备序列号")
    private String deviceSn;

    /// 设备名称
    @Schema(description = "设备名称")
    private String deviceName;

    /// 厂商门店ID
    @Schema(description = "厂商门店ID")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String shopId;

    /// 设备状态
    /// @see cn.daxpay.open.payment.device.enums.DeviceStatusEnum
    @Schema(description = "设备状态")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String status;
}
