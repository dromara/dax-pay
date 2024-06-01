package cn.daxpay.single.sdk.model.assist;

import cn.daxpay.single.sdk.net.DaxPayResponseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信oauth2授权的url连接
 * @author xxm
 * @since 2024/2/10
 */
@Getter
@Setter
@ToString(callSuper = true)
public class WxAuthUrlModel extends DaxPayResponseModel {

    /** 微信oauth2授权的url连接 */
    private String url;
}
