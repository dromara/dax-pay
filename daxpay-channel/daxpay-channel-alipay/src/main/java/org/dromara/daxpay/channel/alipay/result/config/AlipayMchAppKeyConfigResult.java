package org.dromara.daxpay.channel.alipay.result.config;

import org.dromara.daxpay.platform.common.json.sensitive.SensitiveInfo;
import org.dromara.daxpay.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝直连商户应用密钥配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付宝直连商户应用密钥配置")
public class AlipayMchAppKeyConfigResult extends BaseResult {

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "关联应用ID")
    private Long appId;

    @Schema(description = "认证类型")
    private String authType;

    @SensitiveInfo(front = 12, end = 12)
    @Schema(description = "支付宝公钥")
    private String alipayPublicKey;

    @SensitiveInfo(front = 12, end = 12)
    @Schema(description = "应用私钥")
    private String privateKey;

    @SensitiveInfo(front = 12, end = 12)
    @Schema(description = "应用公钥证书")
    private String appCert;

    @SensitiveInfo(front = 12, end = 12)
    @Schema(description = "支付宝公钥证书")
    private String alipayCert;

    @SensitiveInfo(front = 12, end = 12)
    @Schema(description = "支付宝CA根证书")
    private String alipayRootCert;

    @SensitiveInfo
    @Schema(description = "AES通信密钥")
    private String secretKey;
}
