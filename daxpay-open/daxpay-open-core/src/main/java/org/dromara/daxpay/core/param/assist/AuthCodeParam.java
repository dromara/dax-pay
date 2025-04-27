package org.dromara.daxpay.core.param.assist;

import org.dromara.daxpay.core.enums.ChannelAuthTypeEnum;
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

    /**
     * 认证类型, 如果通道支持多种类型的情况下, 参数必传
     * @see ChannelAuthTypeEnum
     */
    @Schema(description = "认证类型")
    private String authType;

    @Schema(description = "标识码")
    @NotBlank(message = "标识码不可为空")
    private String authCode;

    /**
     * App 标识，通过用户请求的 userAgent 中 appUpIdentifier 截取获得，
     * 银联支付标识的格式为“UnionPay/<版本号> <App 标识>”例如 UnionPay/1.0 Cloudpay ，
     * 其中 Cloudpay 即为 App 标识
     */
    @Schema(description = "云闪付App标识")
    private String unionIdentifier;

    /** 用于查询Code值, 可以为空 */
    @Schema(description = "查询Code")
    private String queryCode;

}
