package cn.daxpay.open.channel.alipay.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝服务商代运营授权参数
///
/// 用于生成授权链接, 或 H5 回调时用授权码换取 app_auth_token。
///
@Data
@Accessors(chain = true)
@Schema(title = "支付宝服务商代运营授权参数")
public class AlipayIsvAuthParam {

    /// 通道商户号(定位 alipay_isv_channel_merchant)
    @Schema(description = "通道商户号")
    @NotBlank(message = "{validation.field.channelMchNo.notBlank}")
    private String channelMchNo;

    /// 应用授权码(支付宝回调回传的 app_auth_code, 换 token 时必填)
    @Schema(description = "应用授权码")
    private String code;
}
