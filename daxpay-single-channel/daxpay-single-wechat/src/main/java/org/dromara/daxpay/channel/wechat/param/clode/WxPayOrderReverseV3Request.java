package org.dromara.daxpay.channel.wechat.param.clode;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <pre>
 *  V3撤销订单请求对象类
 * </pre>
 *
 * @author xxm
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class WxPayOrderReverseV3Request implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;
    /**
     * <pre>
     * 字段名：应用ID
     * 变量名：appid
     * 是否必填：是
     * 类型：string[1,32]
     * 描述：
     *  由微信生成的应用ID，全局唯一。请求统一下单接口时请注意APPID的应用属性，例如公众号场景下，需使用应用属性为公众号的APPID
     *  示例值：wxd678efh567hg6787
     * </pre>
     */
    @SerializedName(value = "appid")
    protected String appid;
    /**
     * <pre>
     * 字段名：直连商户号
     * 变量名：mchid
     * 是否必填：是
     * 类型：string[1,32]
     * 描述：
     *  直连商户的商户号，由微信支付生成并下发。
     *  示例值：1230000109
     * </pre>
     */
    @SerializedName(value = "mchid")
    protected String mchid;
}
