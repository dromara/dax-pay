package cn.daxpay.single.core.param.assist;

import cn.daxpay.single.core.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 构造微信OAuth2授权的url链接
 * @author xxm
 * @since 2024/6/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "构造微信OAuth2授权的url链接")
public class AliPayAuthUrlParam extends PaymentCommonParam {

    @Schema(description = "回调地址")
    private String url;

    /**
     * 重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
     */
    @Schema(description = "重定向后原样带回，可以为空")
    private String state;
}
