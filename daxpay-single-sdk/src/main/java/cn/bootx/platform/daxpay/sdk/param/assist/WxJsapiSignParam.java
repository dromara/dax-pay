package cn.bootx.platform.daxpay.sdk.param.assist;

import cn.bootx.platform.daxpay.sdk.model.assist.WxJsapiSignModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayRequest;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 微信预付订单再次签名参数
 * @author xxm
 * @since 2024/2/10
 */
@Setter
@Getter
public class WxJsapiSignParam extends DaxPayRequest<WxJsapiSignModel>{

    /** 预付订单ID */
    private String prepayId;


    /**
     * 方法请求路径
     *
     * @return 请求路径
     */
    @Override
    public String path() {
        return "/unipay/assist/getWxJsapiPrePay";
    }

    /**
     * 将请求返回结果反序列化为实体类
     *
     * @param json json字符串
     * @return 反序列后的对象
     */
    @Override
    public DaxPayResult<WxJsapiSignModel> toModel(String json) {
        return JSONUtil.toBean(json, new TypeReference<DaxPayResult<WxJsapiSignModel>>() {}, false);
    }
}
