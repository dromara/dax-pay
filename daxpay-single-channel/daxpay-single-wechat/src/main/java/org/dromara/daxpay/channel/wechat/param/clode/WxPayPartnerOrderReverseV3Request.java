package org.dromara.daxpay.channel.wechat.param.clode;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 *  微信服务商V3撤销订单请求对象类
 * 参考接口地址: <a href="https://pay.weixin.qq.com/docs/partner/apis/partner-code-payment-v3/partner/partner-reverse.html">...</a>
 * @author xxm
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class WxPayPartnerOrderReverseV3Request implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;
    /**
     * <pre>
     * 字段名：服务商应用ID
     * 变量名：spAppid
     * 是否必填：是
     * 类型：string[1,32]
     * 描述：
     *  由微信生成的应用ID，全局唯一。请求统一下单接口时请注意APPID的应用属性，例如公众号场景下，需使用应用属性为公众号的APPID
     *  示例值：wxd678efh567hg6787
     * </pre>
     */
    @SerializedName(value = "sp_appid")
    protected String spAppid;
    /**
     * <pre>
     * 字段名：服务商商户号
     * 变量名：spMchid
     * 是否必填：是
     * 类型：string[1,32]
     * 描述：
     *  服务商商户号，由微信支付生成并下发。
     *  示例值：1230000109
     * </pre>
     */
    @SerializedName(value = "sp_mchid")
    protected String spMchId;
    /**
     * <pre>
     * 字段名：子商户应用ID
     * 变量名：subAppid
     * 是否必填：否
     * 类型：string[1,32]
     * 描述：
     *  由微信生成的应用ID，全局唯一。请求统一下单接口时请注意APPID的应用属性，例如公众号场景下，需使用应用属性为公众号的APPID
     *  示例值：wxd678efh567hg6787
     * </pre>
     */
    @SerializedName(value = "sub_appid")
    protected String subAppid;
    /**
     * <pre>
     * 字段名：子商户商户号
     * 变量名：subMchid
     * 是否必填：是
     * 类型：string[1,32]
     * 描述：
     *  子商户商户号，由微信支付生成并下发。
     *  示例值：1230000109
     * </pre>
     */
    @SerializedName(value = "sub_mchid")
    protected String subMchId;
}
