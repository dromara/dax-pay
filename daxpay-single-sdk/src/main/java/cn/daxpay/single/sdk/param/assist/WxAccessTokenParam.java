package cn.daxpay.single.sdk.param.assist;

import cn.daxpay.single.sdk.model.assist.WxAccessTokenModel;
import cn.daxpay.single.sdk.net.DaxPayRequest;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 获取微信AccessToken参数
 * @author xxm
 * @since 2024/2/10
 */
@Setter
@Getter
public class WxAccessTokenParam extends DaxPayRequest<WxAccessTokenModel> {

    /** 微信code */
    private String code;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/assist/getWxAccessToken";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<WxAccessTokenModel> toModel(String json) {
        return JSONUtil.toBean(json, new TypeReference<DaxPayResult<WxAccessTokenModel>>() {}, false);
    }
}
