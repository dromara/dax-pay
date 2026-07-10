package cn.daxpay.open.payment.device.qrcode.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 码牌绑定商户参数
@Data
@Accessors(chain = true)
@Schema(title = "码牌绑定商户参数")
public class DeviceQrCodeBindMerchantParam {

    /// 码牌主键列表
    @Schema(description = "码牌主键列表")
    @NotEmpty(message = "{validation.field.ids.notEmpty}")
    private List<Long> ids;

    /// 商户号
    @Schema(description = "商户号")
    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    private String mchNo;

    /// 关联应用号(可空, 空则使用商户默认应用)
    @Schema(description = "关联应用号")
    @Size(max = 50, message = "{validation.field.appId.size}")
    private String appId;
}
