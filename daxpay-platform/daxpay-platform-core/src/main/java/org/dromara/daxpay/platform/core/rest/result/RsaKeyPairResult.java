package org.dromara.daxpay.platform.core.rest.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// # RSA密钥对结果
///
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RsaKeyPairResult {

    @Schema(description = "公钥PEM格式")
    private String publicKey;

    @Schema(description = "私钥PEM格式")
    private String privateKey;
}
