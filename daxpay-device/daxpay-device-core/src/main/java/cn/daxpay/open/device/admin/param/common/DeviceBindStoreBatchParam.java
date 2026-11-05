package cn.daxpay.open.device.admin.param.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 虚拟设备批量绑定门店参数(运营端门店工作台「绑定收银设备到本店」磁贴)
///
/// 设备主键列表 + 门店号, 一次勾选多台批量绑定; 任一设备不存在/归属不符整批拒绝。
@Data
@Accessors(chain = true)
@Schema(title = "虚拟设备批量绑定门店参数")
public class DeviceBindStoreBatchParam {

    /// 设备主键列表
    @Schema(description = "设备主键列表")
    @NotEmpty(message = "{validation.field.ids.notEmpty}")
    private List<Long> deviceIds;

    /// 门店号(须存在且与设备所绑商户一致)
    @Schema(description = "门店号")
    @NotBlank(message = "{validation.field.storeNo.notBlank}")
    @Size(max = 64, message = "{validation.field.storeNo.size}")
    private String storeNo;
}
