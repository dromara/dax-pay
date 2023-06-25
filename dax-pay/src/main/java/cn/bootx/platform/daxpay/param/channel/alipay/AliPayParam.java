package cn.bootx.platform.daxpay.param.channel.alipay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xxm
 * @since 2021/2/27
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付宝支付参数")
public class AliPayParam implements Serializable {

    private static final long serialVersionUID = 7467373358780663978L;

    @Schema(description = "授权码(主动扫描用户的付款码)")
    private String authCode;

    @Schema(description = "页面跳转同步通知页面路径")
    private String returnUrl;

}
