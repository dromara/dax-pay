package org.dromara.daxpay.channel.alipay.result.config;

import cn.bootx.platform.common.jackson.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付宝配置
 * @author xxm
 * @since 2024/6/25
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付宝配置")
public class AlipaySubConfigResult {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 支付宝特约商户Token */
    @SensitiveInfo
    @Schema(description = "支付宝特约商户Token")
    private String appAuthToken;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;

    /**
     * 是商家与支付宝签约后，商家获得的支付宝商家唯一识别码，以 2088 开头的 16 位数字组成，在开放平台中账户中心获取
     */
    @Schema(description = "支付宝商家唯一识别码")
    private String alipayUserId;

    /** 商户AppId */
    @Schema(description = "商户AppId")
    private String appId;

}
