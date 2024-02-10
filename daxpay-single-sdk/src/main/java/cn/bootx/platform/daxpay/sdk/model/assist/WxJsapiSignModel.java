package cn.bootx.platform.daxpay.sdk.model.assist;

import cn.bootx.platform.daxpay.sdk.net.DaxPayResponseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信Jsapi预支付签名返回信息
 * @author xxm
 * @since 2024/2/10
 */
@Getter
@Setter
@ToString
public class WxJsapiSignModel extends DaxPayResponseModel {

    /** 公众号ID */
    private String appId;
    /** 时间戳(秒) */
    private String timeStamp;
    /** 随机串 */
    private String nonceStr;
    /** 预支付ID, 已经是 prepay_id=xxx 格式 */
    private String prePayId;
    /** 微信签名方式 */
    private String signType;
    /** 微信签名 */
    private String paySign;


}
