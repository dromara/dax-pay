package cn.daxpay.open.channel.fuyou.result.rsa;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 富友 RSA 密钥对配置结果
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "富友 RSA 密钥对配置")
public class FuyouRsaKeyConfigResult extends BaseResult {

    @Schema(description = "平台商户号")
    private String mchNo;

    @Schema(description = "通道商户号(FUYOU+雪花)")
    private String channelMchNo;

    @Schema(description = "所属支付产品(fuyou_internet / fuyou_h5)")
    private String product;

    @Schema(description = "富友商户号(mchnt_cd)")
    private String mchntCd;

    @SensitiveInfo(front = 6, end = 4)
    @Schema(description = "商户RSA私钥(PKCS1/PKCS8, 加密存储)")
    private String privateKey;

    @SensitiveInfo(front = 6, end = 4)
    @Schema(description = "富友平台RSA公钥(X509, 加密存储)")
    private String publicKey;

    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;
}
