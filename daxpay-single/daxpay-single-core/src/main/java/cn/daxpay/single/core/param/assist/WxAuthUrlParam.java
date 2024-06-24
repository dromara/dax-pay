package cn.daxpay.single.core.param.assist;

import cn.daxpay.single.core.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;

/**
 * 构造微信OAuth2授权的url链接
 * @author xxm
 * @since 2024/2/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "构造微信OAuth2授权的url链接")
public class WxAuthUrlParam extends PaymentCommonParam {

    @Schema(description = "回调地址")
    @Size(max = 200, message = "回调地址不可超过200位")
    private String url;

    /**
     * 重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
     */
    @Schema(description = "重定向后原样带回，可以为空")
    @Size(max = 128, message = "state值最大为128位")
    private String state;
}
