package cn.daxpay.open.plugin.easypay.result.config;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "易支付凭证结果")
public class EasyPayCredentialResult extends MchBaseResult {

    @Schema(description = "易支付商户号")
    private Integer pid;

    @Schema(description = "应用号")
    private String appId;

    @Schema(description = "启用")
    private Boolean enable;

    @Schema(description = "开启V1")
    private Boolean enableV1;

    @Schema(description = "开启V2")
    private Boolean enableV2;

    @Schema(description = "MD5密钥")
    private String md5Key;

    @Schema(description = "使用系统密钥")
    private Boolean useSystemKey;

    @Schema(description = "商户RSA公钥")
    private String publicKey;

    @Schema(description = "平台RSA公钥")
    private String platformPublicKey;

    @Schema(description = "V1对接地址")
    private String easyPayV1ApiUrl;

    @Schema(description = "V2对接地址")
    private String easyPayV2ApiUrl;
}
