package cn.daxpay.single.sdk.model.assist;

import lombok.Data;

/**
 * 微信AccessToken
 * @author xxm
 * @since 2024/2/10
 */
@Data
public class WxAccessTokenModel{

    /** 微信AccessToken, 目前无返回 */
    private String accessToken;

    /** 微信用户唯一标识 */
    private String openId;
}
