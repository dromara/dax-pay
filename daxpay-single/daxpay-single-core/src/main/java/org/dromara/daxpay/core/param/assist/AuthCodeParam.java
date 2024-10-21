package org.dromara.daxpay.core.param.assist;

import jakarta.validation.constraints.Size;
import org.dromara.daxpay.core.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 通道认证参数
 * @author xxm
 * @since 2024/9/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "通道认证参数")
public class AuthCodeParam extends PaymentCommonParam {

    @Size(max = 32, message = "支付通道不可超过32位")
    @Schema(description = "支付通道")
    @NotBlank(message = "支付通道不可为空")
    private String channel;

    @Size(max = 200, message = "认证标识码不可超过200位")
    @Schema(description = "认证标识码")
    @NotBlank(message = "认证标识码不可为空")
    private String authCode;

    /** 用于查询Code值, 可以为空 */
    @Size(max = 64, message = "查询码不可超过64位")
    @Schema(description = "查询码")
    private String queryCode;

}
