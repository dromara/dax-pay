package cn.daxpay.open.device.admin.param.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 虚拟设备绑定商户参数
///
/// 绑定商户(可一并选应用/门店)、绑定应用两操作共用: appId/storeNo 均可空
/// (空=商户维度通用设备/未分配门店)。
@Data
@Accessors(chain = true)
@Schema(title = "虚拟设备绑定商户参数")
public class DeviceBindMerchantParam {

    /// 设备主键列表
    @Schema(description = "设备主键列表")
    @NotEmpty(message = "{validation.field.ids.notEmpty}")
    private List<Long> ids;

    /// 商户号
    @Schema(description = "商户号")
    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    private String mchNo;

    /// 关联应用号(可空, 空=全部应用)
    @Schema(description = "关联应用号(可空)")
    @Size(max = 50, message = "{validation.field.appId.size}")
    private String appId;

    /// 绑定门店号(可空, 空=未分配门店)
    @Schema(description = "绑定门店号(可空)")
    @Size(max = 50, message = "{validation.field.storeNo.size}")
    private String storeNo;
}
