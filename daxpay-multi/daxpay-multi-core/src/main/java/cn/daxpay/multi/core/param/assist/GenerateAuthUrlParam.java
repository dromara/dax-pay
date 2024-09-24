package cn.daxpay.multi.core.param.assist;

import cn.daxpay.multi.core.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
     * 通道
     */
    @Schema(description = "通道")
    private String channel;

    /**
     * 自定义授权重定向回调地址, 如果不传, 使用系统提供的默认地址
     */
    @Schema(description = "自定义授权回调地址")
    private String authRedirectUrl;

}
