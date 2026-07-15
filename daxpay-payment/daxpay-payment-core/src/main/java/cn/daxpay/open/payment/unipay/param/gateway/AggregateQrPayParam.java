package cn.daxpay.open.payment.unipay.param.gateway;

import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/// # 聚合扫码发起支付参数(H5 侧)
@Data
@Schema(title = "聚合扫码支付参数")
public class AggregateQrPayParam {

    @Schema(description = "平台网关单号")
    @NotBlank(message = "{validation.field.orderNo.notBlank}")
    @Size(max = 64, message = "{validation.field.orderNo.size}")
    private String orderNo;

    /// 客户端环境（UA/宿主识别，与聚合/收银台配置共用 [ClientEnvEnum]）
    /// @see ClientEnvEnum
    @Schema(description = "客户端环境(ClientEnvEnum: wechat_pay/alipay/union_pay/douyin/browser)")
    @NotBlank(message = "{validation.field.clientEnv.notBlank}")
    @Size(max = 32, message = "{validation.field.clientEnv.size}")
    private String clientEnv;

    @Schema(description = "OpenId(微信 JSAPI 等)")
    @Size(max = 128, message = "{validation.field.openId.size}")
    private String openId;

    @Schema(description = "设备(mobile/pc, 与 H5 __DEVICE__ 一致)")
    @Size(max = 16, message = "{validation.field.device.size}")
    private String device;

    @Schema(description = "客户端IP")
    @Size(max = 64, message = "{validation.field.clientIp.size}")
    private String clientIp;
}
