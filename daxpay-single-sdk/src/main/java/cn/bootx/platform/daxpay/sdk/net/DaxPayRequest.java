package cn.bootx.platform.daxpay.sdk.net;

import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
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

    /** 方法请求路径 */
    public abstract String path();

    /**
     * 将请求返回结果反序列化为实体类
     */
    public abstract DaxPayResult<T> toModel(String json);


    /** 客户端ip */
    private String clientIp;

    /** 商户扩展参数,回调时会原样返回 */
    private String attach;

    /** 是否不进行同步通知的跳转 */
    private boolean notReturn;

    /** 同步跳转URL, 部分接口不支持该配置，传输了也不会生效 */
    private String returnUrl;

    /** 是否不启用异步通知 */
    private boolean notNotify;

    /** 异步通知地址 */
    private String notifyUrl;

    /** 签名 */
    private String sign;

    /** API版本号 */
    private String version = "1.0";

    /** 请求时间，传输时间戳 */
    private Long reqTime = DateUtil.currentSeconds();

}
