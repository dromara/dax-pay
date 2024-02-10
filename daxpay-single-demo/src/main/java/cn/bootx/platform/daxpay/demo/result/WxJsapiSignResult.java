package cn.bootx.platform.daxpay.demo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信Jsapi预支付签名返回信息
 * @author xxm
 * @since 2024/2/10
 */
@Data
@Accessors(chain = true)
@Schema(title = "微信Jsapi预支付签名返回信息")
public class WxJsapiSignResult {

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
