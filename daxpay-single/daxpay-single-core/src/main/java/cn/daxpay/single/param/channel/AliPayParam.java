package cn.daxpay.single.param.channel;

import cn.daxpay.single.param.ChannelParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xxm
 * @since 2021/2/27
 */
@Data
@Schema(title = "支付宝支付参数")
public class AliPayParam implements ChannelParam {

    @Schema(description = "授权码(主动扫描用户的付款码)")
    private String authCode;

}
