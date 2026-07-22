package cn.daxpay.open.payment.unipay.param.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/// # 网关 H5 生成授权链接参数(无商户签名)
///
/// 凭网关订单装载商户上下文后生成 OAuth 链接; 用于收银台/聚合等落地页取 openId。
/// 微信/抖音按支付路由读通道商户应用; 支付宝仍走平台级配置。
@Data
@Schema(title = "网关授权链接参数")
public class GatewayAuthUrlParam {

    @Schema(description = "平台网关单号")
    @NotBlank(message = "{validation.field.orderNo.notBlank}")
    @Size(max = 64, message = "{validation.field.orderNo.size}")
    private String orderNo;

    /// 认证类型: wechat / alipay / douyin(与 ChannelAuthTypeEnum / ClientEnv 对齐)
    @Schema(description = "认证类型 wechat/alipay/douyin")
    @NotBlank(message = "{validation.field.authType.notBlank}")
    @Size(max = 32, message = "{validation.field.authType.size}")
    private String authType;

    /// 授权完成后前端回跳路径, 须为站内相对路径(如 /cashier/{orderNo}/wechat)
    @Schema(description = "授权完成后回跳路径")
    @NotBlank(message = "{validation.field.returnPath.notBlank}")
    @Size(max = 200, message = "{validation.field.returnPath.size}")
    private String returnPath;

    /// 客户端环境(聚合/收银台 H5 解析支付方式用; 支付宝可空)
    /// @see cn.daxpay.open.payment.merchant.enums.ClientEnvEnum
    @Schema(description = "客户端环境 wechat/alipay/douyin/union_pay")
    @Size(max = 32, message = "{validation.field.clientEnv.size}")
    private String clientEnv;

    /// 运行形态(聚合解析用; 空默认 h5)
    /// @see cn.daxpay.open.payment.merchant.enums.ClientRuntimeEnum
    @Schema(description = "运行形态 h5/mini")
    @Size(max = 16, message = "{validation.field.runtime.size}")
    private String runtime;

    /// 收银台支付项 ID(收银台场景必填)
    @Schema(description = "收银台支付项ID")
    private Long itemId;

    /// 收银台类型 h5/web/mini(收银台场景必填)
    /// @see cn.daxpay.open.payment.merchant.enums.GatewayCashierTypeEnum
    @Schema(description = "收银台类型")
    @Size(max = 16, message = "{validation.field.cashierType.size}")
    private String cashierType;
}
