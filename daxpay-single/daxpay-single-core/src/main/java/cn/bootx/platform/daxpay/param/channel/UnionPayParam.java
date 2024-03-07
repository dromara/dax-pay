package cn.bootx.platform.daxpay.param.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 云闪付支付参数
 * @author xxm
 * @since 2024/3/6
 */
@Data
@Schema(title = "云闪付支付参数")
public class UnionPayParam {

    @Schema(description = "微信openId")
    private String openId;

    @Schema(description = "授权码(主动扫描用户的付款码)")
    private String authCode;

    /**
     * 银联JS支付时进行传输
     */
    @Schema(description = "用户ID")
    private String userId;

    /**
     * 用户的外网ip，需要与访问银联支付页面的ip一致，银联会进行校验
     */
    @Schema(description = "用户ip")
    private String customerIp;

    /**
     * 商户app对应的微信开放平台移动应用APPID
     * 微信APP支付时进行传输
     */
    @Schema(description = "移动应用APPID")
    private String appId;

    /**
     * 微信开放平台审核通过的移动应用AppID
     * 微信APP支付时进行传输
     */
    @Schema(description = "移动应用AppID")
    private String subAppId;

    /**
     * 买家支付宝用户ID
     * 支付宝服务窗时传输
     */
    @Schema(description = "微信支付授权码")
    private String buyerId;

}
