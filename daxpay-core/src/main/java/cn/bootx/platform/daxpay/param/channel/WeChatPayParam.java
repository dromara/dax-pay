package cn.bootx.platform.daxpay.param.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @since 2021/6/21
 */
@Data
@Accessors(chain = true)
@Schema(title = "微信支付参数")
public class WeChatPayParam {

    @Schema(description = "微信openId")
    private String openId;

    @Schema(description = "授权码(主动扫描用户的付款码)")
    private String authCode;

}
