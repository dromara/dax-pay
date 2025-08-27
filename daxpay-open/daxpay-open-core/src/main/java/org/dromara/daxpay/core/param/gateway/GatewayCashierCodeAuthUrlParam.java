package org.dromara.daxpay.core.param.gateway;

import org.dromara.daxpay.core.enums.CashierSceneEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 码牌认证链接生成参数
 * @author xxm
 * @since 2025/4/12
 */
@Data
@Accessors(chain = true)
@Schema(title = "码牌认证链接生成参数")
public class GatewayCashierCodeAuthUrlParam {

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
}
