package cn.daxpay.open.device.virtual.client.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 虚拟设备激活参数(免登录)
///
/// 开源版单因子激活(拍板: 砍激活码体系): 设备号 SN 为唯一准入凭据,
/// 缓解 = 21 亿随机空间 + 激活限流(IP 10/min + SN 20/min) + 可禁用 + 重复激活轮换 secret。
@Data
@Accessors(chain = true)
@Schema(title = "虚拟设备激活参数")
public class VirtualDeviceActivateParam {

    /// 设备码即设备号 SN(运营绑定商户后发放, APP 凭设备码激活; 支持扫码带入服务器地址)
    @NotBlank(message = "{validation.field.deviceSn.notBlank}")
    @Schema(description = "设备码(设备号 SN)")
    private String sn;
}
