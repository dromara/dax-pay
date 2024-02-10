package cn.bootx.platform.daxpay.sdk.model.assist;

import cn.bootx.platform.daxpay.sdk.net.DaxPayResponseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信AccessToken
 * @author xxm
 * @since 2024/2/10
 */
@Getter
@Setter
@ToString
public class WxAccessTokenModel extends DaxPayResponseModel {

    /** 微信AccessToken */
    private String accessToken;

    /** 微信用户唯一标识 */
    private String openId;
}
