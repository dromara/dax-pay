package org.dromara.daxpay.channel.alipay.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付宝特约商户配置参数
 * @author xxm
 * @since 2024/6/26
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付宝特约商户配置参数")
public class AlipaySubConfigParam {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 支付宝特约商户Token */
    @NotBlank(message = "支付宝AppAuthToken不可为空")
    @Schema(description = "支付宝特约商户Token")
    private String appAuthToken;

    /** 是否启用 */
    @Schema(description = "是否启用")
    @NotNull(message = "是否启用不可为空")
    private Boolean enable;

    /** 商户号 */
    @NotBlank(message = "商户号不可为空")
    @Schema(description = "商户号")
    private String mchNo;

    /** 商户AppId */
    @Schema(description = "商户AppId")
    @NotBlank(message = "商户AppId不可为空")
    private String appId;


}
