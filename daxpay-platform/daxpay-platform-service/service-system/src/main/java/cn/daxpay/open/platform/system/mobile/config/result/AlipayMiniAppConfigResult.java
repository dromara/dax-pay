package cn.daxpay.open.platform.system.mobile.config.result;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝小程序应用配置出参(敏感字段脱敏)
@Data
@Accessors(chain = true)
@Schema(title = "支付宝小程序应用配置")
public class AlipayMiniAppConfigResult {

    @Schema(description = "小程序 AppId")
    private String appId;

    @Schema(description = "鉴权方式: public_key / cert")
    private String authType;

    @SensitiveInfo(front = 24, end = 24)
    @Schema(description = "应用私钥")
    private String privateKey;

    @SensitiveInfo(front = 24, end = 24)
    @Schema(description = "支付宝公钥")
    private String alipayPublicKey;

    @SensitiveInfo(front = 24, end = 24)
    @Schema(description = "应用公钥证书")
    private String appCert;

    @SensitiveInfo(front = 24, end = 24)
    @Schema(description = "支付宝公钥证书")
    private String alipayCert;

    @SensitiveInfo(front = 24, end = 24)
    @Schema(description = "支付宝根证书")
    private String alipayRootCert;
}
