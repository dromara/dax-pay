package cn.daxpay.open.payment.unipay.param.gateway;

import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import cn.daxpay.open.payment.merchant.enums.GatewayCashierTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/// # 网关收银台发起支付参数(H5/WEB 侧)
///
/// 用户点选支付项后提交; 服务端按 itemId 读取配置解析支付方式, 不信任前端传入 method/通道字段。
@Data
@Schema(title = "收银台支付参数")
public class CashierPayParam {

    @Schema(description = "平台网关单号")
    @NotBlank(message = "{validation.field.orderNo.notBlank}")
    @Size(max = 64, message = "{validation.field.orderNo.size}")
    private String orderNo;

    @Schema(description = "收银台支付项ID")
    @NotNull(message = "{validation.field.id.notNull}")
    private Long itemId;

    /// @see GatewayCashierTypeEnum
    @Schema(description = "收银台类型 h5/web")
    @NotBlank(message = "{validation.field.cashierType.notBlank}")
    @Size(max = 16, message = "{validation.field.cashierType.size}")
    private String cashierType;

    /// H5 必填; WEB 可空
    /// @see ClientEnvEnum
    @Schema(description = "客户端环境(H5 必填; WEB 可空)")
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
