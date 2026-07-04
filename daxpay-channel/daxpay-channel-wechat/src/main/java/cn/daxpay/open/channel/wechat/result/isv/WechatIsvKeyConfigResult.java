package cn.daxpay.open.channel.wechat.result.isv;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信服务商密钥配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "微信服务商密钥配置")
public class WechatIsvKeyConfigResult extends BaseResult {

    @Schema(description = "产品编码")
    private String product;

    @Schema(description = "微信服务商商户号")
    private String wxMchId;

    @SensitiveInfo(front = 12, end = 12)
    @Schema(description = "API V3密钥(加密存储)")
    private String apiKeyV3;

    @SensitiveInfo(front = 12, end = 12)
    @Schema(description = "支付公钥(加密存储)")
    private String publicKey;

    @Schema(description = "支付公钥ID")
    private String publicKeyId;

    @SensitiveInfo(front = 12, end = 12)
    @Schema(description = "apiclient_key证书(加密存储)")
    private String privateKey;

    @SensitiveInfo(front = 12, end = 12)
    @Schema(description = "apiclient_cert证书(加密存储)")
    private String privateCert;

    @Schema(description = "证书序列号")
    private String certSerialNo;
}
