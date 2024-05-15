package cn.daxpay.single.sdk.param.assist;

import cn.daxpay.single.sdk.model.assist.WxAuthUrlModel;
import cn.daxpay.single.sdk.net.DaxPayRequest;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 构造oauth2授权的url连接
 * @author xxm
 * @since 2024/2/10
 */
@Setter
@Getter
public class WxAuthUrlParam extends DaxPayRequest<WxAuthUrlModel> {

    /** 回调地址 */
    private String url;

    /**
     * 重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
     */
    private String state;

    /**
     * 方法请求路径
     *
     * @return 请求路径
     */
    @Override
    public String path() {
        return "/unipay/assist/getWxAuthUrl";
    }

    /**
     * 将请求返回结果反序列化为实体类
     *
     * @param json json字符串
     * @return 反序列后的对象
     */
    @Override
    public DaxPayResult<WxAuthUrlModel> toModel(String json) {
        return JSONUtil.toBean(json, new TypeReference<DaxPayResult<WxAuthUrlModel>>() {}, false);

    }
}
