package cn.daxpay.open.payment.unipay.param.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/// # 网关 H5 生成授权链接参数(无商户签名)
///
/// 凭网关订单装载商户上下文后生成 OAuth 链接; 用于收银台/聚合等落地页取 openId。
/// 当前一期走平台级认证配置(微信公众号/支付宝/抖音 H5); 通道级(按支付项 DIRECT)可后续扩展。
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
}
