package cn.bootx.platform.daxpay.service.param.system.config;

import cn.bootx.platform.daxpay.code.PaySignTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 平台配置
 * @author xxm
 * @since 2024/1/2
 */
@Data
@Accessors(chain = true)
@Schema(title = "平台配置")
public class PlatformConfigParam {

    @Schema(description = "网站地址")
    private String websiteUrl;

    /**
     * @see PaySignTypeEnum
     */
    @Schema(description = "签名方式")
    private String signType;

    @Schema(description = "签名秘钥")
    private String signSecret;

    @Schema(description = "支付通知地址")
    private String notifyUrl;

    @Schema(description = "同步支付跳转地址")
    private String returnUrl;

    @Schema(description = "订单默认超时时间(分钟)")
    private Integer orderTimeout;

    @Schema(description = "订单支付限额(分)")
    private Integer limitAmount;
}
