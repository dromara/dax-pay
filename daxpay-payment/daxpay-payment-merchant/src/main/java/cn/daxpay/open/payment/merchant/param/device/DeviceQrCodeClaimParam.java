package cn.daxpay.open.payment.merchant.param.device;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 码牌认领参数
///
/// 商户将平台空白库存码牌(未分配商户)按编码认领到自己商户名下
/// 应用与门店为可选归属, 认领时一并落库可省去事后二次设置; 留空则支付时取商户默认应用/默认门店
@Data
@Accessors(chain = true)
@Schema(title = "码牌认领参数")
public class DeviceQrCodeClaimParam {

    /// 码牌编码(印制在码牌物料上, 扫码链接尾段同值)
    @Schema(description = "码牌编码")
    @NotBlank(message = "{validation.field.code.notBlank}")
    @Size(max = 100, message = "{validation.field.code.size}")
    private String code;

    /// 关联应用号(可空, 空则使用商户默认应用)
    @Schema(description = "关联应用号")
    @Size(max = 50, message = "{validation.field.appId.size}")
    private String appId;

    /// 绑定门店号(可空; 有值须归属本商户)
    @Schema(description = "绑定门店号(可空)")
    @Size(max = 64, message = "{validation.field.storeNo.size}")
    private String storeNo;
}
