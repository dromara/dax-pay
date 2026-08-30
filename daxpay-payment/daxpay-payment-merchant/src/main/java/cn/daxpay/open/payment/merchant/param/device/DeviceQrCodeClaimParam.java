package cn.daxpay.open.payment.merchant.param.device;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 码牌认领参数
///
/// 商户将平台空白库存码牌(未分配商户)按编码认领到自己商户名下
@Data
@Accessors(chain = true)
@Schema(title = "码牌认领参数")
public class DeviceQrCodeClaimParam {

    /// 码牌编码(印制在码牌物料上, 扫码链接尾段同值)
    @Schema(description = "码牌编码")
    @NotBlank(message = "{validation.field.code.notBlank}")
    @Size(max = 100, message = "{validation.field.code.size}")
    private String code;
}
