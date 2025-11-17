package org.dromara.daxpay.payment.common.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 平台OCR配置参数
 * @author xxm
 * @since 2025/9/14
 */
@Data
@Accessors(chain = true)
@Schema(title = "平台OCR配置参数")
public class PlatformOcrConfigParam {

    /** ID */
    @Schema(description = "ID")
    private Long id;

    /** OCR供应商 */
    @NotBlank(message = "OCR供应商不能为空")
    @Schema(description = "OCR供应商")
    private String provider;

    /** endPoint */
    @Schema(description = "endPoint")
    private String endpoint;

    /** accessKey */
    @Schema(description = "accessKey")
    private String accessKey;

    /** secretKey */
    @Schema(description = "secretKey")
    private String secretKey;
}
