package org.dromara.daxpay.payment.unipay.param.gateway;

import org.dromara.daxpay.payment.unipay.enums.CashierSceneEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 码牌OpenId标识认证类
 * @author xxm
 * @since 2025/4/12
 */
@Data
@Accessors(chain = true)
@Schema(title = "码牌OpenId标识认证参数")
public class CashierCodeAuthParam {

    /** 码牌编码 */
    @NotBlank(message = "码牌编码不可为空")
    @Schema(description = "码牌编码")
    private String code;

    /**
     * 支付场景
     * @see CashierSceneEnum
     */
    @NotBlank(message = "支付场景不可为空")
    @Schema(description = "支付场景")
    private String scene;

    /** 认证Code */
    @NotNull(message = "认证Code不可为空")
    @Schema(description = "认证Code")
    private String authCode;

}
