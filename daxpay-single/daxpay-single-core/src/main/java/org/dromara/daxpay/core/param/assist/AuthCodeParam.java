package org.dromara.daxpay.core.param.assist;

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

    @Schema(description = "通道")
    @NotBlank(message = "通道不可为空")
    private String channel;

    @Schema(description = "标识码")
    @NotBlank(message = "标识码不可为空")
    private String authCode;

    /** 用于查询Code值, 可以为空 */
    @Schema(description = "查询Code")
    private String queryCode;

}
