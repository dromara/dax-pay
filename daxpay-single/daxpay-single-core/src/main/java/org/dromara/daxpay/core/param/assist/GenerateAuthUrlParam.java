package org.dromara.daxpay.core.param.assist;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.param.PaymentCommonParam;

/**
 * 生成授权链接参数
 * @author xxm
 * @since 2024/9/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "生成授权链接参数")
public class GenerateAuthUrlParam extends PaymentCommonParam {

    /**
     * 支付通道
     */
    @NotBlank(message = "支付通道必填")
    @Schema(description = "支付通道")
    private String channel;

    /**
     * 自定义授权重定向地址, 如果不传, 使用系统提供的默认地址
     */
    @Schema(description = "授权重定向地址")
    @Size(max = 100, message = "授权重定向地址不可超过100位")
    private String authRedirectUrl;

}
