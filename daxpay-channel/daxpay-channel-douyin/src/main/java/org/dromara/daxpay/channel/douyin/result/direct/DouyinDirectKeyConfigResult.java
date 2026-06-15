package org.dromara.daxpay.channel.douyin.result.direct;

import org.dromara.daxpay.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 抖音直连密钥配置结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "抖音直连密钥配置结果")
public class DouyinDirectKeyConfigResult extends MchBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "商户私钥(已脱敏)")
    private String merchantPrivateKey;

    @Schema(description = "商家公钥证书序列号")
    private String merchantSerialNumber;

    @Schema(description = "接口加密密钥(已脱敏)")
    private String encryptKey;

    @Schema(description = "私钥是否已配置")
    private boolean privateKeyConfigured;

    @Schema(description = "加密密钥是否已配置")
    private boolean encryptKeyConfigured;
}
