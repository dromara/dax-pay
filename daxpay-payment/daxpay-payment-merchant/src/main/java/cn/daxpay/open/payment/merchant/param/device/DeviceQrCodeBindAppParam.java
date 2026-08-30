package cn.daxpay.open.payment.merchant.param.device;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 码牌绑定应用参数(商户端)
@Data
@Accessors(chain = true)
@Schema(title = "码牌绑定应用参数(商户端)")
public class DeviceQrCodeBindAppParam {

    /// 码牌主键列表
    @Schema(description = "码牌主键列表")
    @NotEmpty(message = "{validation.field.ids.notEmpty}")
    private List<Long> ids;

    /// 应用号(须归属当前商户)
    @Schema(description = "应用号")
    @NotBlank(message = "{validation.field.appId.notBlank}")
    @Size(max = 50, message = "{validation.field.appId.size}")
    private String appId;
}
