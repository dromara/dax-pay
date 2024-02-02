package cn.bootx.platform.daxpay.param.channel;

import cn.bootx.platform.daxpay.param.ChannelParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xxm
 * @since 2021/6/21
 */
@Data
@Schema(title = "微信支付参数")
public class WeChatPayParam implements ChannelParam {

    @Schema(description = "微信openId")
    private String openId;

    @Schema(description = "授权码(主动扫描用户的付款码)")
    private String authCode;

}
