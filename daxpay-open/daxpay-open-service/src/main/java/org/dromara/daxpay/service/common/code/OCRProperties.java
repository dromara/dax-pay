package org.dromara.daxpay.service.common.code;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bootx-platform.starter.ocr")
@Setter
@Getter
public class OCRProperties {

    /** OCR类型  tencent/aliyun */
    private String type;

    /** key */
    private String accessKey;

    /** 密钥 */
    private String secretKey;

    /** endPoint */
    private String endPoint;

}
