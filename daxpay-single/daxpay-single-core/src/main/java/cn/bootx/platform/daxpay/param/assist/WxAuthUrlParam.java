package cn.bootx.platform.daxpay.param.assist;

import cn.bootx.platform.daxpay.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 构造oauth2授权的url连接
 * @author xxm
 * @since 2024/2/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "构造oauth2授权的url连接")
public class WxAuthUrlParam extends PaymentCommonParam {

    @Schema(description = "回调地址")
    private String url;

    /**
     * 重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
     */
    @Schema(description = "重定向后原样带回，可以为空")
    private String state;
}
