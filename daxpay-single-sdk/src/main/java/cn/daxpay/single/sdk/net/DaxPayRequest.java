package cn.daxpay.single.sdk.net;

import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.hutool.core.date.DateUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 请求接口
 * @author xxm
 * @since 2024/2/2
 */
@Getter
@Setter
public abstract class DaxPayRequest<T extends DaxPayResponseModel> {

    /** 客户端ip */
    private String clientIp;

    /** 签名 */
    private String sign;

    /** 请求时间，传输时间戳 */
    private Long reqTime = DateUtil.currentSeconds();

    /** 随机数 */
    private String nonceStr;

    /**
     * 方法请求路径
     * @return 请求路径
     */
    public abstract String path();

    /**
     * 将请求返回结果反序列化为实体类
     * @param json json字符串
     * @return 反序列后的对象
     */
    public abstract DaxPayResult<T> toModel(String json);

}
