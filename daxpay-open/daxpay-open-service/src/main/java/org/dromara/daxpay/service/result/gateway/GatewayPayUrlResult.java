package org.dromara.daxpay.service.result.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 网关支付响应参数
 * @author xxm
 * @since 2024/11/26
 */
@Data
@Accessors(chain = true)
@Schema(title = "网关支付响应参数")
public class GatewayPayUrlResult {

    @Schema(description = "收银台链接")
    private String url;

    @Schema(description = "收银台发起信息")
    private String payBody;
}
