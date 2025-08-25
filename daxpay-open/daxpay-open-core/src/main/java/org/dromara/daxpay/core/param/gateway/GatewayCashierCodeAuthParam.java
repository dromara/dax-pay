package org.dromara.daxpay.core.param.gateway;

import org.dromara.daxpay.core.enums.CashierSceneEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 码牌用户标识认证类
 * @author xxm
 * @since 2025/4/12
 */
@Data
@Accessors(chain = true)
@Schema(title = "码牌用户标识认证类")
public class GatewayCashierCodeAuthParam {

    /** 码牌编码 */
    @NotBlank(message = "码牌编码不可为空")
    @Schema(description = "码牌编码")
    private String cashierCode;

    /**
     * 支付场景
     * @see CashierSceneEnum
     */
    @NotBlank(message = "支付场景不可为空")
    @Schema(description = "支付场景")
    private String cashierScene;

    /** 认证Code */
    @NotNull(message = "认证Code不可为空")
    @Schema(description = "认证Code")
    private String authCode;

}
