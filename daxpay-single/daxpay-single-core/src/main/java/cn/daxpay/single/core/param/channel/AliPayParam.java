package cn.daxpay.single.core.param.channel;

import cn.daxpay.single.core.param.ChannelParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 支付宝支付参数
 * @author xxm
 * @since 2021/2/27
 */
@Data
@Schema(title = "支付宝支付参数")
public class AliPayParam implements ChannelParam {

    /**
     * 授权码(主动扫描用户的付款码)
     */
    @Schema(description = "授权码(主动扫描用户的付款码)")
    private String authCode;

    /**
     * 【描述】小程序支付中，商户实际经营主体的小程序应用的appid，也即最终唤起收银台支付所在的小程序的应用id
     * 【注意事项】商户需要先在产品管理中心绑定该小程序appid，否则下单会失败
     */
    @Schema(description = "商户实际经营主体的小程序应用的appid")
    private String opAppId;

    /**
     * 买家支付宝用户唯一标识
     */
    @Schema(description = "买家支付宝用户唯一标识")
    private String openId;

}
